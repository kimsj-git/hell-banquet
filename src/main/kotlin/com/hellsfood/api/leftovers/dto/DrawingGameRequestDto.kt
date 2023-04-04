package com.hellsfood.api.leftovers.dto

data class DrawingGameRequestDto(
	val userId: String,
	val today: String,
	val status: String,
	val propName: String
)
