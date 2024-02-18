package dev.eknath.aiconvo.ui.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RiddleData(val question: String, val answer: String)