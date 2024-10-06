package dev.eknath.aiconvo.presentation.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.eknath.aiconvo.presentation.data.models.RiddleData
import dev.eknath.aiconvo.presentation.enums.PROMPT_ACTIVITY
import dev.eknath.aiconvo.presentation.presentation.states.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RiddleViewModel(private val generativeModel: GenerativeModel) : ViewModel() {

    //currentRiddle
    private val _riddle: MutableStateFlow<Content<RiddleData?>> =
        MutableStateFlow(Content(data = null))
    val riddle = _riddle


    //previous riddles
    private val _riddles: MutableStateFlow<Content<Set<RiddleData?>>> =
        MutableStateFlow(Content(data = emptySet()))
    val riddles = _riddles


    fun fetchARiddle() {
        viewModelScope.launch {
            _riddle.update { it.copy(state = UiState.Loading) }

            val riddleResponse = generativeModel.generateContent(PROMPT_ACTIVITY.RIDDLE.prompt)
            val response = riddleData(riddleResponse.text?.cleanJson().orEmpty())

            riddle.value.data?.let {
                _riddles.update { it.copy(data = it.data.plus(it.data)) }
            }

            _riddle.update {
                it.copy(
                    data = response,
                    state = if (response == null) UiState.Error else UiState.Success
                )
            }
            Log.e(
                "Test",
                "Question: ${riddle.value.data?.question} Answer: ${riddle.value.data?.answer}"
            )
        }
    }
}

internal fun riddleData(input: String): RiddleData? {
    val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val adapter = moshi.adapter(RiddleData::class.java)
    return try {
        adapter.fromJson(input)
    } catch (e: Exception) {
        null
    }
}

class RiddleViewModelFactory(private val generativeModel: GenerativeModel) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RiddleViewModel::class.java)) {
            return RiddleViewModel(generativeModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
