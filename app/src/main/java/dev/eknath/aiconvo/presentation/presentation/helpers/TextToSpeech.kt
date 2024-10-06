package dev.eknath.aiconvo.presentation.presentation.helpers

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import java.util.Locale

@Composable
fun rememberTTS(context: Context, enableMaleVoice: Boolean = true): State<TextToSpeech?> {
    val textToSpeechService: MutableState<TextToSpeech?> = remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        textToSpeechService.value = TextToSpeech(
            context
        ) { tStatus ->
            if (tStatus == TextToSpeech.SUCCESS) {
                textToSpeechService.value?.language = Locale.getDefault()

                //TODO let user select the voices as per their wishes
                if (enableMaleVoice) {
                    val availableVoices =
                        textToSpeechService.value?.voices?.filter { it.locale == Locale.getDefault() }
                    // Find a suitable male voice (modify the conditions as needed)
                    availableVoices?.forEach {  Log.e("TTS",it.toString())}
                    val maleVoice =
                        availableVoices?.find { it.name.contains("en-us-x-iom-local", ignoreCase = true) }
                    // If a male voice is found, attempt to set it
                    maleVoice?.let { textToSpeechService.value?.setVoice(it) }

                }

            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            textToSpeechService.value?.stop()
            textToSpeechService.value?.shutdown()
        }
    }
    return textToSpeechService
}

fun State<TextToSpeech?>.speak(
    text: String,
    onSuccess: () -> Unit = {},
    onFailure: () -> Unit = {}
) {
    when (this.value?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)) {
        TextToSpeech.SUCCESS -> onSuccess()
        TextToSpeech.ERROR -> onFailure()
    }
}
