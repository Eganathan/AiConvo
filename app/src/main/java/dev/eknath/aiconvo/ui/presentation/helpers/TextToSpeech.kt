package dev.eknath.aiconvo.ui.presentation.helpers

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import java.util.Locale

@Composable
fun rememberTTS(context: Context): State<TextToSpeech?> {
    val textToSpeechService: MutableState<TextToSpeech?> = remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        textToSpeechService.value = TextToSpeech(
            context
        ) { tStatus ->
            if (tStatus == TextToSpeech.SUCCESS) {
                textToSpeechService.value?.language = Locale.getDefault()
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
