package dev.eknath.aiconvo.presentation.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MathChallenge(val question: String, val answer: String, val explanation: String)