package com.hellsfood.api.leftovers.data

import lombok.Builder
import org.hibernate.annotations.DynamicInsert
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDate
import javax.persistence.*

@DynamicInsert
@Entity
@Builder
class Leftover {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(length = 16)
    lateinit var userId: String

    var before: Int = 0

    var after: Int = -1

    var percentage: Double = -1.0

    @CreatedDate
    var date: LocalDate = LocalDate.now()

}