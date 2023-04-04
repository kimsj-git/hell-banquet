package com.hellsfood.common

import java.time.LocalDateTime

data class JanbaniDto(
    val id: Long,
    val userId: String,
    val feature: String,
    val janbanCode: String,
    val updateAt: LocalDateTime
)