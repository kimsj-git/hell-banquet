package com.hellsfood.api.leftovers.service

import com.hellsfood.api.leftovers.data.Leftover
import com.hellsfood.api.leftovers.data.LeftoverRepository
import com.hellsfood.api.leftovers.data.Ranking
import com.hellsfood.api.leftovers.data.RankingRepository
import com.hellsfood.api.leftovers.dto.LeftoverRegisterRequestDto
import com.hellsfood.api.users.data.User
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
    private val rankingRepository: RankingRepository
) {
    @Transactional
    fun registerLeftover(requestDto: LeftoverRegisterRequestDto): Boolean {
        val userId: String = requestDto.userId
        val amount: Int = requestDto.amount
        if (amount == -1) {
            return false
        }
        var date = LocalDate.now()
        var leftover: Leftover
        if (leftoverRepository.existsByUserIdAndDate(userId, date)) {
            leftover = leftoverRepository.findByUserIdAndDate(userId, date)
            leftover.after = amount
            leftover.percentage = leftover.after * 1.0 / leftover.before
        } else {
            leftover = Leftover()
            leftover.userId = userId
            leftover.before = amount
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
    }

    @Transactional
    fun resetRankingTable(){
        rankingRepository.deleteAll()
    }

    @Transactional
    fun setRankingTable(rankingList:List<Ranking>){
        rankingRepository.saveAll(rankingList)
    }

    fun getRankingList(userId: String): List<Ranking> {
        val rankingList: MutableList<Ranking> = rankingRepository.findAll().toMutableList();
        if (userId.isNotEmpty()) {
            rankingRepository.findByUserId(userRepository.findByUserId(userId))?.let { rankingList.add(it) }
        }
        return rankingList
    }
}