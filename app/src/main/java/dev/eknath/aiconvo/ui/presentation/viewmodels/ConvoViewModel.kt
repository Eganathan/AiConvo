package dev.eknath.aiconvo.ui.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
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
    private val _mathChallenge: MutableStateFlow<MathChallenge?> = MutableStateFlow(null)
    val mathChallenge = _mathChallenge.asStateFlow()

    //currentRiddle
    private val _riddle: MutableState<RiddleData?> = mutableStateOf(null)
    val riddle = _riddle

    //news
    private val _news: MutableStateFlow<ContentState<News?>> = MutableStateFlow(ContentState(null, state = UiState.Initial))
    val news: StateFlow<ContentState<News?>> = _news.asStateFlow()

    init {
        fetchATechQuote()
    }

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

        viewModelScope.launch {
            try {
                val response = prompt(inputText)
                response?.text?.let { outputContent ->
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

    fun fetchATechQuote() {
        viewModelScope.launch {
            val techQuote = prompt(PROMPT_ACTIVITY.TECH_QUOTE.prompt)
            _quote.value = techQuote(techQuote?.text?.cleanJson().orEmpty())
        }
    }

    fun fetchARiddle() {
        viewModelScope.launch {
            val riddleResponse = prompt(PROMPT_ACTIVITY.RIDDLE.prompt)
            _riddle.value = riddleData(riddleResponse?.text?.cleanJson().orEmpty())
            Log.e("Test", "Question: ${riddle.value?.question} Answer: ${riddle.value?.answer}")
        }
    }

    fun fetchMathChallenge() {
        viewModelScope.launch {
            val promptResponse = prompt(PROMPT_ACTIVITY.MATH_CHALLENGE.prompt)
            _mathChallenge.update { mathChallengeData(promptResponse?.text?.cleanJson().orEmpty()) }
            Log.e(
                "Test",
                "MATH RES:${promptResponse?.text} feedback: ${promptResponse?.promptFeedback}"
            )
            Log.e(
                "Test",
                "MATH:  Question: ${mathChallenge.value?.question} Answer: ${mathChallenge.value?.answer} EXP: ${mathChallenge.value?.explanation}"
            )
        }
    }

    fun fetchNews() {
        viewModelScope.launch {
            Log.e("Test", "Loading")
            _news.update { it.copy(state = UiState.Loading) }
            val promptResponse = prompt(PROMPT_ACTIVITY.TECH_AND_SCIENCE_NEWS.prompt)
            val response = newsData(promptResponse?.text?.cleanJson().orEmpty())

            if (response != null) {
                _news.update { it.copy(value = response, state = UiState.Success) }
                Log.e("Test", "Res: ${news.value.value?.news?.map { it.headline }}")
            } else {
                _news.update { it.copy(value = null, state = UiState.Error) }
                Log.e("Test", "Failed")
            }
        }
    }

    private suspend fun prompt(input: String): GenerateContentResponse? {
        return try {
            generativeModel.generateContent(input)
        } catch (e: Exception) {
            Log.e("Error", e.localizedMessage.orEmpty())
            null
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

@JsonClass(generateAdapter = true)
data class NewsItem(
    val type: String,
    val headline: String,
    val summary: String,
    val link: String,
    val imageLink: String? = null
)

@JsonClass(generateAdapter = true)
data class News(val news: List<NewsItem>)


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


fun newsData(input: String): News? {
    val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val adapter = moshi.adapter(News::class.java)
    return try {
        Log.e("Test", "INPUT:$input")
        adapter.fromJson(input)
    } catch (e: Exception) {
        Log.e("Test", "${e.message}")
        null
    }
}

data class ContentState<T>(
    val value: T,
    val state: UiState = UiState.Loading,
)