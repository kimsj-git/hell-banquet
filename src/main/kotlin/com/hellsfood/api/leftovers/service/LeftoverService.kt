package com.hellsfood.api.leftovers.service

import com.hellsfood.api.leftovers.data.*
import com.hellsfood.api.leftovers.dto.LeftoverRegisterRequestDto
import com.hellsfood.api.users.data.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
@RequiredArgsConstructor
class LeftoverService(
    private val leftoverRepository: LeftoverRepository,
    private val userRepository: UserRepository,
    private val rankingRepository: RankingRepository,
    private val analysisRepository: AnalysisRepository
) {

    @Transactional
    fun registerLeftover(requestDto: LeftoverRegisterRequestDto): Boolean {
        if (requestDto.amount == -1) {
            return false
        }
        var date = LocalDate.now()
        var leftover: Leftover
        if (leftoverRepository.existsByUserIdAndDate(requestDto.userId, date)) {
            leftover = leftoverRepository.findByUserIdAndDate(requestDto.userId, date)
            leftover.after = requestDto.amount
            leftover.percentage = leftover.after * 1.0 / leftover.before
        } else {
            leftover = Leftover()
            leftover.userId = requestDto.userId
            leftover.before = requestDto.amount
            leftover.course = requestDto.courseNo
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

        calculateDailyAnalysis();
    }

    @Transactional
    fun calculateDailyAnalysis() {
        for (i in 1..2) run {
            val courseInfo = Analysis()
            courseInfo.served = leftoverRepository.getBeforeSumByDateAndCourse(LocalDate.now(), i)
            courseInfo.leftovers = leftoverRepository.getAfterSumByDateAndCourse(LocalDate.now(), i)
            courseInfo.courseNo = i
            analysisRepository.save(courseInfo)
        }
    }

    fun getAnalysisByDateRange(startDate: Int, endDate: Int): List<Analysis> {
        return analysisRepository.getCourseInfoByDateRange(parseDate(startDate), parseDate(endDate))
    }

    private fun parseDate(date: Int?): LocalDate {
        if (date != null) {
            val year = date / 10000
            val mmDd = date % 10000
            val month = mmDd / 100
            val day = mmDd % 100
            return LocalDate.of(year, month, day)
        } else {
            val currentTimeInSeoul = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Asia/Seoul"))
            if (currentTimeInSeoul.hour < 14) {
                return currentTimeInSeoul.minusDays(1).toLocalDate()
            } else {
                return currentTimeInSeoul.toLocalDate()
            }
        }
    }

    @Transactional
    fun resetRankingTable() {
        rankingRepository.deleteAll()
    }

    @Transactional
    fun setRankingTable(rankingList: List<Ranking>) {
        rankingRepository.saveAll(rankingList)
    }

    fun getRankingList(userId: String, integerDate: Int?): List<Ranking> {
        val searchDate = parseDate(integerDate)
        val rankingList = rankingRepository.findAllByDate(searchDate)
        if (userId.isNotEmpty()) {
            rankingRepository.findByUserId(userId, dateOfToday = searchDate)?.let { rankingList.add(it) }
        }
        return rankingList
    }

}