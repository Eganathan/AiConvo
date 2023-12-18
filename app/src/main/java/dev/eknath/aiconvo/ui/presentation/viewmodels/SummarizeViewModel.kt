package dev.eknath.aiconvo.ui.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.eknath.aiconvo.ui.enums.PROMPT_ACTIVITY
import dev.eknath.aiconvo.ui.presentation.states.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConvoViewModel(
    private val generativeModel: GenerativeModel
) : ViewModel() {

    //conversations
    private val _covUiData: MutableStateFlow<List<Conv>> = MutableStateFlow(emptyList())
    val covUiData: StateFlow<List<Conv>> = _covUiData.asStateFlow()

    //currentQuote
    private val _quote: MutableState<QuoteData?> = mutableStateOf(null)
    val techQuote = _quote

    //currentQuote
    private val _mathChallenge: MutableState<MathChallenge?> = mutableStateOf(null)
    val mathChallenge = _mathChallenge

    //currentRiddle
    private val _riddle: MutableState<RiddleData?> = mutableStateOf(null)
    val riddle = _riddle

    fun generateContent(inputText: String) {
        val inputConv =
            Conv(owner = Owner.USER, value = inputText, state = UiState.Success)
        val aiPreConv = Conv(owner = Owner.AI, value = "", state = UiState.Loading)

        _covUiData.update {
            it.plus(inputConv)
        }
        _covUiData.update {
            it.plus(aiPreConv)
        }


        val prompt = "$inputText"
        viewModelScope.launch {
            try {
                val response = generativeModel.generateContent(prompt)
                response.text?.let { outputContent ->
                    _covUiData.update {
                        it.dropLast(1)
                    }
                    _covUiData.update {
                        it.plus(
                            Conv(
                                id = aiPreConv.id,
                                owner = aiPreConv.owner,
                                value = outputContent,
                                state = UiState.Success
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                _covUiData.update {
                    it.minus(aiPreConv)
                }
                _covUiData.update {
                    it.plus(
                        Conv(
                            id = aiPreConv.id,
                            owner = aiPreConv.owner,
                            value = e.localizedMessage ?: "Something Went Wrong!",
                            state = UiState.Error
                        )
                    )
                }
            }
        }
    }

    init {
        fetchATechQuote()
        fetchMathChallenge()
    }


    private fun fetchATechQuote() {
        viewModelScope.launch {
            val techQuote = generativeModel.generateContent(PROMPT_ACTIVITY.TECH_QUOTE.prompt)
            _quote.value = techQuote(techQuote.text?.cleanJson().orEmpty())
        }
    }

    fun fetchARiddle() {
        viewModelScope.launch {
            val riddleResponse = generativeModel.generateContent(PROMPT_ACTIVITY.RIDDLE.prompt)
            _riddle.value = riddleData(riddleResponse.text?.cleanJson().orEmpty())
            Log.e("Test", "Question: ${riddle.value?.question} Answer: ${riddle.value?.answer}")
        }
    }

    fun fetchMathChallenge() {
        viewModelScope.launch {
            val mathChallengeResponse =
                generativeModel.generateContent(PROMPT_ACTIVITY.MATH_PROBLEM.prompt)
            _mathChallenge.value =
                mathChallengeData(mathChallengeResponse.text?.cleanJson().orEmpty())
            Log.e("Test", "MATH RES:${mathChallengeResponse.text}")
            Log.e(
                "Test",
                "MATH:  Question: ${mathChallenge.value?.question} Answer: ${mathChallenge.value?.answer} EXP: ${mathChallenge.value?.explanation}"
            )
        }
    }

}

//datas
class Conv(
    val id: Long = System.currentTimeMillis(),
    val owner: Owner,
    val value: String,
    val state: UiState
)

enum class Owner { AI, USER }


fun String.cleanJson(): String {
    return this.replace("`", "").replace("json", "")
}


@JsonClass(generateAdapter = true)
data class QuoteData(val quote: String, val author: String)

@JsonClass(generateAdapter = true)
data class RiddleData(val question: String, val answer: String)


@JsonClass(generateAdapter = true)
data class MathChallenge(val question: String, val answer: String, val explanation: String)

fun techQuote(input: String): QuoteData? {
    val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val adapter = moshi.adapter(QuoteData::class.java)
    return try {
        adapter.fromJson(input)
    } catch (e: Exception) {
        null
    }
}


fun riddleData(input: String): RiddleData? {
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


fun mathChallengeData(input: String): MathChallenge? {
    val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val adapter = moshi.adapter(MathChallenge::class.java)
    return try {
        adapter.fromJson(input)
    } catch (e: Exception) {
        null
    }
}