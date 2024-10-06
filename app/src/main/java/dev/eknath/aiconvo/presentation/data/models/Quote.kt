package dev.eknath.aiconvo.presentation.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuoteData(val quote: String, val author: String)