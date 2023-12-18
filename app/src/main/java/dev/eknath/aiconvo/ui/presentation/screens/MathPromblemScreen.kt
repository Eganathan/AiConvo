package dev.eknath.aiconvo.ui.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.input.TextFieldValue
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
            var revelaAnswer by remember{ mutableStateOf(false) }

            Text(text = "Question:")
            Text(text = challenge?.question.orEmpty())
            Divider()
            Text(text = "Answer:")
            Text(text = challenge?.answer.orEmpty())
            Divider()
            Text(text = "Explanation:")
            Text(text = challenge?.explanation.orEmpty())
            Divider()

        }
    }

    LaunchedEffect(challenge) {
        if (challenge == null) {
            viewModel.fetchMathChallenge()
            Log.e("Test", "Fired!")
        }
    }
}