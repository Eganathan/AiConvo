package dev.eknath.aiconvo

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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

    fun generateContent(inputText: String) {
        val inputConv =
            Conv(owner = Owner.USER, value = inputText, state = SummarizeUiState.Success(""))
        val aiPreConv = Conv(owner = Owner.AI, value = "", state = SummarizeUiState.Loading)

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
                                state = SummarizeUiState.Success("")
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
                            state = SummarizeUiState.Error(e.localizedMessage ?: "Some Error!")
                        )
                    )
                }
            }
        }
    }

    init {
        fetchATechQuote()
    }

    fun fetchATechQuote() {
        viewModelScope.launch {
            val techQuote = generativeModel.generateContent(ACTIVITY.TECH_QUOTE.prompt)
            _quote.value = techQuote(techQuote.text?.cleanJson().orEmpty())
        }
    }

}

class Conv(
    val id: Long = System.currentTimeMillis(),
    val owner: Owner,
    val value: String,
    val state: SummarizeUiState
)

enum class Owner {
    AI, USER
}

enum class ACTIVITY(val prompt: String) {
    TECH_QUOTE("Give me a single random tech related quote and author in a json format like quote=$ and author=$"),
    FUNNY_JOCK("Share a funny clean jock"),
    TONGUE_TWISTER("give me a plain tongue twister"),
    RIDDLE("Give me a riddle with answer as a json format like question=\$ and answer=\$"),
    MATH_PROBLEM("Give me a fun math problem with answer is a json format like question=\$ and answer=\$"),
    NONE("");
}

fun String.cleanJson(): String {
    return this.replace("`", "").replace("json", "")
}





@JsonClass(generateAdapter = true)
data class QuoteData(val quote: String, val author: String)

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