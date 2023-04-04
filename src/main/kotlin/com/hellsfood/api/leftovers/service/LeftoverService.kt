package com.hellsfood.api.leftovers.service

import com.hellsfood.api.leftovers.data.Analysis
import com.hellsfood.api.leftovers.data.AnalysisRepository
import com.hellsfood.api.leftovers.data.Leftover
import com.hellsfood.api.leftovers.data.LeftoverRepository
import com.hellsfood.api.leftovers.data.Ranking
import com.hellsfood.api.leftovers.data.RankingRepository

import com.hellsfood.api.leftovers.dto.LeftoverRegisterRequestDto
import com.hellsfood.client.UploadServiceClient
import com.hellsfood.common.JanbaniUpdateRequestDto
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
    private val analysisRepository: AnalysisRepository,
    private val uploadServiceClient: UploadServiceClient
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
        val rankingList = mutableListOf<Ranking>()
        for (l: Leftover in leftoverList) {
            val cur = Ranking()
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
     * today(yyyy-MM-dd)에 userId의 잔반이가 있는지 확인하는 함수
     */
    fun hasLeftoverAndJanbani(userId: String, today: String): Boolean {
        getLeftoverByUserIdAndDate(userId, today)
        return uploadServiceClient.hasJanbani(userId, today)
    }

    fun isPlayableCookieGame(userId: String, today: String): Boolean {
        val leftover = getLeftoverByUserIdAndDate(userId, today)

        if (!hasLeftoverAndJanbani(userId, today) && leftover.percentage > -1) {   //잔반이가 없으며, percentage가 -1이 아니라면?
            return true
        }
        return false
    }

    fun isPlayableDrawingGame(userId: String, today: String): Boolean {
        val leftover = getLeftoverByUserIdAndDate(userId, today)

        if (!hasLeftoverAndJanbani(userId, today) || leftover.propStatus.equals("not assigned") || leftover.propStatus.equals("used")) {   //잔반이가 없으며, percentage가 -1이 아니라면?
            return false
        }
        return true
    }

    @Transactional
    fun updatePropStatus(userId: String, today: String, status: String, propName: String): Leftover {
        val leftover = getLeftoverByUserIdAndDate(userId, today)

        if (status.equals("assign") && leftover.propStatus.equals("not assigned")) {
            leftover.propStatus = "assigned"
        } else if (status.equals("change") && leftover.propStatus.equals("assigned")) {
            uploadServiceClient.updateJanbaniCode(JanbaniUpdateRequestDto(userId, propName))
            leftover.propStatus = "used"
        }
        leftoverRepository.save(leftover)
        return leftover
    }

}