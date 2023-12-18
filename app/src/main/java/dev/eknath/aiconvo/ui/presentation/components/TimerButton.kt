package dev.eknath.aiconvo.ui.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun <T> TimerButton(
    waitTime: Long = 6000,
    key: T,
    onClick: () -> Unit
) {
    var enabled by remember { mutableStateOf(false) }
    var waitedTime by remember { mutableLongStateOf(waitTime) }
    val waitTimeInString by remember { derivedStateOf { "" + (waitedTime / 100).toString() + " Sec" } }

    Button(
        enabled = enabled,
        onClick = onClick
    ) {
        if (!enabled)
            Row {
                Text(text = waitTimeInString)
            }
        else
            Text(text = "Answer")
    }

    LaunchedEffect(key1 = key) {
        waitedTime = waitTime
    }

    LaunchedEffect(true) {
        while (waitedTime > 0) {
            delay(1000)
            waitedTime -= 100
        }
        enabled = true
    }
}