package dev.eknath.aiconvo.ui.presentation.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import dev.eknath.aiconvo.ui.presentation.components.TimerButton
import dev.eknath.aiconvo.ui.presentation.viewmodels.ConvoViewModel


@Composable
internal fun MathChallengeScreen(data: ScreenParams) {

    val viewModel = remember { ConvoViewModel(data.generativeViewModel) }
    val challenge by viewModel.mathChallenge.collectAsState()

    Scaffold(topBar = {
        ActivityScreenTopBar(title = "Math Challenge",
            enabled = true,
            onBackPressed = { data.navController.navigateUp() })
    }) {
        Column(Modifier.padding(it)) {

            var inputTextField by remember { mutableStateOf(TextFieldValue()) }
            var revealAnswer by remember { mutableStateOf(false) }
            var showExplanation by remember { mutableStateOf(false) }

            Text(text = "Question:")
            Text(text = challenge?.question.orEmpty())
            Divider()

            AnimatedVisibility(visible = !revealAnswer, exit = slideOutHorizontally(), enter =slideInHorizontally()) {
                TimerButton(key = challenge?.question) {
                    revealAnswer = true
                }
            }

            if (revealAnswer) {
                if (showExplanation) {
                    Text(text = "Explanation:")
                    Text(text = challenge?.explanation.orEmpty())
                    Divider()
                } else {
                    Text(text = "Answer:")
                    Text(text = challenge?.answer.orEmpty())
                    Text(
                        text = "explanation here",
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { showExplanation = true })
                    Divider()
                }
            }

            Button(
                onClick = {
                    viewModel.fetchMathChallenge()
                    revealAnswer = false
                    showExplanation = false
                    inputTextField = TextFieldValue()
                }) {
                if (revealAnswer)
                    Text("Next")
                else
                    Text("Skip")

            }


        }
    }

    LaunchedEffect(challenge) {
        if (challenge == null) {
            viewModel.fetchMathChallenge()
            Log.e("Test", "Fired!")
        }
    }
}