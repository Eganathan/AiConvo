package dev.eknath.aiconvo.presentation.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.eknath.aiconvo.presentation.presentation.components.TimerButton
import dev.eknath.aiconvo.presentation.presentation.helpers.shareNote
import dev.eknath.aiconvo.presentation.presentation.states.UiState


@Composable
internal fun MathChallengeScreen(data: ScreenParams) {

    val challenge by data.viewModel.mathChallenge.collectAsState()
    val context = LocalContext.current
    Scaffold(topBar = {
        ActivityScreenTopBar(
            title = "Math Challenge",
            enabled = true,
            onBackPressed = { data.navController.navigateUp() })
    }) {
        Column(
            Modifier
                .padding(it)
        ) {
            var inputTextField by remember { mutableStateOf(TextFieldValue()) }
            var revealAnswer by remember { mutableStateOf(false) }
            var showExplanation by remember { mutableStateOf(false) }

            AnimatedVisibility(
                visible = (challenge.data?.question?.isBlank() == false && challenge.state is UiState.Success),
                enter = fadeIn() + scaleIn(tween(300)),
                exit = fadeOut() + scaleOut(tween(300))
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 5.dp)
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                ) {
                    AnimatedVisibility(
                        visible = !revealAnswer,
                        enter = slideInVertically(),
                        exit = slideOutVertically(tween(600))
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .padding(start = 5.dp)
                            ) {
                                Text(
                                    text = "${challenge.data?.question}",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.verticalScroll(rememberScrollState())
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 5.dp, bottom = 5.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    AnimatedVisibility(challenge.data != null) {
                                        IconButton(
                                            modifier = Modifier.size(23.dp),
                                            onClick = {
                                                context.shareNote(
                                                    title = "Can you answer this?: ",
                                                    content = challenge.data?.question.orEmpty()
                                                )
                                            }) {
                                            Icon(
                                                imageVector = Icons.Filled.Share,
                                                contentDescription = ""
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(
                        thickness = 5.dp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth(0.1f)
                            .clip(RoundedCornerShape(5.dp))
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    AnimatedVisibility(visible = !revealAnswer) {
                        TimerButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp),
                            key = challenge.data?.question
                        ) {
                            revealAnswer = true
                        }
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
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

                    if (revealAnswer) {
                        if (showExplanation) {
                            Text(text = "Explanation:")
                            Text(
                                text = challenge.data?.explanation.orEmpty(),
                            )
                            Divider()
                        } else {
                            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                Text(text = "Answer:")
                                Text(text = challenge.data?.answer.orEmpty())
                                Text(
                                    text = "explanation here",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontStyle = FontStyle.Italic,
                                    textDecoration = TextDecoration.Underline,
                                    modifier = Modifier.clickable { showExplanation = true })
                                Spacer(modifier = Modifier.height(40.dp))
                            }
                        }
                    }
                }
            }

        }
    }

    if (challenge.state is UiState.Loading)
        LoadingDialog()

    LaunchedEffect(challenge.data) {
        if (challenge.data == null && challenge.state !is UiState.Loading) {
            data.viewModel.fetchMathChallenge()
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