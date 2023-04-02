package com.hellsfood.api.leftovers.data

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface RankingRepository : JpaRepository<Ranking, Long> {
    fun findByUserId(userId: String, dateOfToday: LocalDate): Ranking?

    fun findAllByDate(dateOfToday: LocalDate):MutableList<Ranking>

}