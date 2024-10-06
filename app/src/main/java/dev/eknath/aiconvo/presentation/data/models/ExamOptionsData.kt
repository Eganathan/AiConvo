package dev.eknath.aiconvo.presentation.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Exam(
    val exam: List<Question>
)

@JsonClass(generateAdapter = true)
data class Question(
    val question: String,
    val options: List<Option>,
    val answer: Int,
    val explanation: String
)

@JsonClass(generateAdapter = true)
data class Option(
    val option_id: Int,
    val option: String
)