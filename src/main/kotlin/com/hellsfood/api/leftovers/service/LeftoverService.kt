package com.hellsfood.api.leftovers.service

import com.hellsfood.api.leftovers.data.Analysis
import com.hellsfood.api.leftovers.data.AnalysisRepository
import com.hellsfood.api.leftovers.data.Leftover
import com.hellsfood.api.leftovers.data.LeftoverRepository
import com.hellsfood.api.leftovers.data.Ranking
import com.hellsfood.api.leftovers.data.RankingRepository

import com.hellsfood.api.leftovers.dto.LeftoverRegisterRequestDto
import lombok.RequiredArgsConstructor
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.transaction.Transactional

@Service
@RequiredArgsConstructor
class LeftoverService(
    private val leftoverRepository: LeftoverRepository,
    private val rankingRepository: RankingRepository,
    private val analysisRepository: AnalysisRepository
) {

    @Transactional
    fun registerLeftover(requestDto: LeftoverRegisterRequestDto): Boolean {
        if (requestDto.amount == -1) {
            return false
        }
        val date = LocalDate.now()
        var leftover: Leftover? = leftoverRepository.findByUserIdAndDate(requestDto.userId, date)
        if (leftover != null) {
            leftover.after = requestDto.amount
            leftover.percentage = leftover.after * 1.0 / leftover.before
        } else {
            leftover = Leftover()
            leftover.userId = requestDto.userId
            leftover.before = requestDto.amount
            leftover.course = requestDto.courseNo
            leftover.date = date
        }
        leftoverRepository.save(leftover)
        return true
    }

    // 평일 14시에 자동으로 잔반 DB를 트리거해서 랭킹 테이블 갱신
    @Scheduled(cron = "0 0 14 * * 1-5", zone = "Asia/Seoul")
    fun refreshRankingTable() {
        resetRankingTable()
        val currentTime: LocalDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Asia/Seoul")).toLocalDate()
        val leftoverList = leftoverRepository.getDailyRanking(currentTime)
        var rankingList = mutableListOf<Ranking>()
        for (l: Leftover in leftoverList) {
            var cur = Ranking()
            cur.userId = l.userId
            cur.leftPercentage = l.percentage
            rankingList.add(cur)
        }
        setRankingTable(rankingList)

        calculateDailyAnalysis()
    }

    @Transactional
    fun calculateDailyAnalysis() {
        for (i in 1..2) run {
            val courseInfo = Analysis()
            courseInfo.served = leftoverRepository.getBeforeSumByDateAndCourse(LocalDate.now(), i) ?: 0
            courseInfo.leftovers = leftoverRepository.getAfterSumByDateAndCourse(LocalDate.now(), i) ?: 0
            courseInfo.courseNo = i
            analysisRepository.save(courseInfo)
        }
    }

    fun getAnalysisByDateRange(startDate: String, endDate: String): List<Analysis> {
        return analysisRepository.getCourseInfoByDateRange(parseDate(startDate), parseDate(endDate))
    }

    private fun parseDate(dateStr: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDate.parse(dateStr, formatter)
    }

    @Transactional
    fun resetRankingTable() {
        rankingRepository.deleteAll()
    }

    @Transactional
    fun setRankingTable(rankingList: List<Ranking>) {
        rankingRepository.saveAll(rankingList)
    }

    fun getRankingList(userId: String): List<Ranking> {
        val rankingList = rankingRepository.findAll()
        if (userId.isNotEmpty()) {
            rankingRepository.findByUserId(userId)?.let { rankingList.add(it) }
        }
        return rankingList
    }

    @Transactional
    fun getLeftoverByUserIdAndDate(userId: String, date: String): Leftover {
        return leftoverRepository.findByUserIdAndDate(userId, parseDate(date))
            ?: throw NoSuchElementException("Leftover not found for user $userId and date $date")
    }

    /**
     * janbani.updateat이 today인지를 판단하여, hasJanbani 값을 가져오는 로직을 추가해야 함 (@FeignClient 이용)
     */
    fun isPlayableCookieGame(userId: String, today: String, hasJanbani: Boolean): Boolean {
        val leftover = leftoverRepository.findByUserIdAndDate(userId, parseDate(today))
            ?: throw NoSuchElementException("Leftover not found for user $userId and date $today")

        if (!hasJanbani && leftover.percentage > -1) {   //잔반이가 없으며, percentage가 -1이 아니라면?
            return true
        }
        return false
    }

    fun isPlayableDrawingGame(userId: String, today: String, hasJanbani: Boolean): Boolean {
        val leftover = leftoverRepository.findByUserIdAndDate(userId, parseDate(today))
            ?: throw NoSuchElementException("Leftover not found for user $userId and date $today")

        if (!hasJanbani || leftover.propStatus.equals("not assigned") || leftover.propStatus.equals("used")) {   //잔반이가 없으며, percentage가 -1이 아니라면?
            return false
        }
        return true
    }

    fun updatePropStatus(userId: String, today: String, hasJanbani: Boolean, newPropStatus: String): Leftover {
        val leftover = leftoverRepository.findByUserIdAndDate(userId, parseDate(today))
            ?: throw NoSuchElementException("Leftover not found for user $userId and date $today")

        // not assigned -> assigned : OK
        // assigned -> used : OK
        if (hasJanbani &&
            ((leftover.propStatus.equals("not assigned") && newPropStatus.equals("assigned"))
                    || (leftover.propStatus.equals("assigned") && newPropStatus.equals("used")))
        ) {
            leftover.propStatus = newPropStatus
            leftoverRepository.save(leftover)
            return leftover
        } else {
            throw IllegalArgumentException("Invalid Value")
        }
    }

}