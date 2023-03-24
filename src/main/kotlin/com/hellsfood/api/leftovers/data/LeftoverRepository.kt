package com.hellsfood.api.leftovers.data

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface LeftoverRepository : JpaRepository<Leftover, Long> {
    fun existsByUserIdAndDate(userId: String, date: LocalDate): Boolean

    fun findByUserIdAndDate(userId: String, date: LocalDate): Leftover

    @Query(
        value = "select * from Leftover l where l.percentage>=0 and l.date=:date order by l.percentage", nativeQuery = true
    )
    fun getDailyRanking(date: LocalDate): List<Leftover>

    @Query(value="select sum(l.before) as served from Leftover l where l.date=:date and l.course=:course")
    fun getBeforeSumByDateAndCourse(date:LocalDate, course:Int): Long

    @Query(value="select sum(l.after) as leftovers from Leftover l where l.date=:date and l.course=:course")
    fun getAfterSumByDateAndCourse(date:LocalDate, course:Int): Long

}