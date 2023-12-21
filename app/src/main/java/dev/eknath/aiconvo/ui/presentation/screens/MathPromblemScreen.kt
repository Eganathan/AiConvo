package dev.eknath.aiconvo.ui.presentation.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.eknath.aiconvo.ui.presentation.components.TimerButton
import dev.eknath.aiconvo.ui.presentation.states.UiState


@Composable
internal fun MathChallengeScreen(data: ScreenParams) {

    val challenge by data.viewModel.mathChallenge.collectAsState()

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
            Text(text = challenge.data?.question.orEmpty())
            Divider()

            AnimatedVisibility(
                visible = !revealAnswer,
                exit = slideOutHorizontally(),
                enter = slideInHorizontally()
            ) {
                TimerButton(key = challenge.data?.question) {
                    revealAnswer = true
                }
            }

            if (revealAnswer) {
                if (showExplanation) {
                    Text(text = "Explanation:")
                    Text(text = challenge.data?.explanation.orEmpty())
                    Divider()
                } else {
                    Text(text = "Answer:")
                    Text(text = challenge.data?.answer.orEmpty())
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
                    data.viewModel.fetchMathChallenge()
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

    if (challenge.state is UiState.Loading)
        LoadingDialog()

    LaunchedEffect(challenge.data) {
        if (challenge.data == null && challenge.state !is UiState.Loading) {
            data.viewModel.fetchMathChallenge()
            Log.e("Test", "Fired!")
        }
    }
}

@Composable
fun LoadingDialog() {
    Dialog(onDismissRequest = { }) {
        Card(modifier = Modifier.wrapContentSize()) {
            Column(
                modifier = Modifier.padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Text(
                    text = "Loading Data...",
                    modifier = Modifier.offset(y = 5.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}