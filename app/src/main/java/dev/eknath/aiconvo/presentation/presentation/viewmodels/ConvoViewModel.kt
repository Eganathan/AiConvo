package dev.eknath.aiconvo.presentation.presentation.viewmodels

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
import dev.eknath.aiconvo.presentation.data.models.MathChallenge
import dev.eknath.aiconvo.presentation.data.models.QuoteData
import dev.eknath.aiconvo.presentation.data.models.RiddleData
import dev.eknath.aiconvo.presentation.enums.PROMPT_ACTIVITY
import dev.eknath.aiconvo.presentation.presentation.states.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConvoViewModel(
    val proModel: GenerativeModel,
    val visionModel: GenerativeModel,
    val correctnessModel: GenerativeModel
) : ViewModel() {

    //conversations
    private val _covUiData: MutableStateFlow<List<Conv>> = MutableStateFlow(emptyList())
    val covUiData: StateFlow<List<Conv>> = _covUiData.asStateFlow()

    //currentQuote
    private val _quote: MutableState<QuoteData?> = mutableStateOf(null)
    val techQuote = _quote

    //currentQuote
    private val _mathChallenge: MutableStateFlow<Content<MathChallenge?>> =
        MutableStateFlow(Content(data = null))
    val mathChallenge = _mathChallenge.asStateFlow()

    //currentRiddle
    private val _riddle: MutableStateFlow<Content<RiddleData?>> =
        MutableStateFlow(Content(data = null))
    val riddle = _riddle

    //summary
    private val _summary: MutableStateFlow<ContentState<String?>> =
        MutableStateFlow(ContentState(null, state = UiState.Success))
    val summary = _summary

    //news
    private val _news: MutableStateFlow<ContentState<News?>> =
        MutableStateFlow(ContentState(null, state = UiState.Initial))
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
            _riddle.update { it.copy(state = UiState.Loading) }

            val riddleResponse = correctnessModel.generateContent(PROMPT_ACTIVITY.RIDDLE.prompt)
            val response = riddleData(riddleResponse.text?.cleanJson().orEmpty())
            _riddle.update {
                it.copy(
                    data = response,
                    state = if (response == null) UiState.Error else UiState.Success
                )
            }
            Log.e(
                "Test",
                "Question: ${riddle.value.data?.question} Answer: ${riddle.value?.data?.answer}"
            )
        }
    }

    fun fetchMathChallenge() {
        viewModelScope.launch {
            _mathChallenge.update { it.copy(state = UiState.Loading) }
            val promptResponse = prompt(PROMPT_ACTIVITY.MATH_CHALLENGE.prompt)
            val data = mathChallengeData(promptResponse?.text?.cleanJson().orEmpty())

            _mathChallenge.update {
                it.copy(
                    data = data,
                    state = if (data != null) UiState.Success else UiState.Error
                )
            }
            Log.e(
                "Test",
                "MATH:  Question: ${mathChallenge.value?.data?.question} Answer: ${mathChallenge.value?.data?.answer} EXP: ${mathChallenge.value?.data?.explanation}"
            )
        }
    }

    fun fetchNews() {
        /*viewModelScope.launch {
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
        }*/
    }

    fun summarizeArticle(url: String) {
        viewModelScope.launch {
            _summary.update { it.copy(state = UiState.Loading, value = null) }
            val promptResponse = prompt(PROMPT_ACTIVITY.SUMMARIZE_ARTICLE.prompt.plus(url))
            _summary.update {
                it.copy(
                    value = promptResponse?.text,
                    state = if (promptResponse?.text?.isNotBlank() == true) UiState.Success else UiState.Error
                )

            }

        }
    }

    private suspend fun prompt(input: String): GenerateContentResponse? {
        return try {
            proModel.generateContent(input)
        } catch (e: Exception) {
            Log.e("Error", e.localizedMessage.orEmpty())
            null
        }
    }

}

data class Content<T>(
    val data: T,
    val state: UiState = UiState.Initial
)

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

data class ContentState<T>(
    val value: T,
    val state: UiState = UiState.Loading,
)