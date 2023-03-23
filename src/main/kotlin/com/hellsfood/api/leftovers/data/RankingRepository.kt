package com.hellsfood.api.leftovers.data

import com.hellsfood.api.users.data.User
import org.springframework.data.jpa.repository.JpaRepository

interface RankingRepository : JpaRepository<Ranking, Long> {
    fun findByUserId(userId: User): Ranking?

}