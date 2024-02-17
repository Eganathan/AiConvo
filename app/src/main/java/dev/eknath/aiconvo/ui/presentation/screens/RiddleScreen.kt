package dev.eknath.aiconvo.ui.presentation.screens

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import dev.eknath.aiconvo.ui.presentation.components.ContentCard
import dev.eknath.aiconvo.ui.presentation.components.DefaultBackButton
import dev.eknath.aiconvo.ui.presentation.components.RiddleInputField
import dev.eknath.aiconvo.ui.presentation.components.TimerButton
import dev.eknath.aiconvo.ui.presentation.helpers.rememberTTS
import dev.eknath.aiconvo.ui.presentation.helpers.shareNote
import dev.eknath.aiconvo.ui.presentation.helpers.speak
import dev.eknath.aiconvo.ui.presentation.states.UiState
import dev.eknath.aiconvo.ui.presentation.viewmodels.ConvoViewModel


@Stable
data class ScreenParams(
    val navController: NavController,
    val viewModel: ConvoViewModel
)

@Composable
fun RiddleScreen(data: ScreenParams) {

    val context = LocalContext.current
    val tts = rememberTTS(context = context)

    var input = remember { mutableStateOf(TextFieldValue()) }
    val riddle by data.viewModel.riddle.collectAsState()
    var revealAnswer by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf(false) }

    val onSubmit = {
        if (input.value.text.lowercase() == riddle.data?.answer?.lowercase()) {
            revealAnswer = false
            data.viewModel.fetchARiddle()
            input.value = TextFieldValue()
        } else {
            error = true
            input.value = TextFieldValue()
        }
    }

    Scaffold(
        topBar = {
            ActivityScreenTopBar(
                title = "Riddle",
                enabled = true,
                onBackPressed = { data.navController.navigateUp() })
        }
    ) {
        Column(Modifier.padding(it)) {
            ContentCard {
                AnimatedVisibility(
                    visible = (riddle.data?.question?.isBlank() == false && riddle.state is UiState.Success),
                    enter = fadeIn() + scaleIn(tween(300)),
                    exit = fadeOut() + scaleOut(tween(300))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 25.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Question",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .padding(start = 5.dp)
                            ) {
                                Text(
                                    text = "${riddle.data?.question}",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 5.dp, bottom = 5.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {

                                    AnimatedVisibility(riddle.data != null) {
                                        IconButton(
                                            modifier = Modifier.size(23.dp),
                                            onClick = {
                                                tts.speak(
                                                    riddle.data?.question
                                                        ?: "Sorry,Some Error Occurred!"
                                                )
                                            }) {
                                            Icon(
                                                imageVector = Icons.Filled.PlayArrow,
                                                contentDescription = ""
                                            )
                                        }
                                    }

                                    AnimatedVisibility(riddle.data != null) {
                                        IconButton(
                                            modifier = Modifier.size(23.dp),
                                            onClick = {
                                                context.shareNote(
                                                    title = "Check out this Riddle: ",
                                                    content = riddle.data?.question.orEmpty()
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
                        Spacer(modifier = Modifier.height(20.dp))
                        Divider(
                            thickness = 5.dp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .fillMaxWidth(0.1f)
                                .clip(RoundedCornerShape(5.dp))
                        )

                        AnimatedVisibility(visible = revealAnswer) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = "Answer",
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .padding(start = 5.dp)
                                    ) {
                                        Text(
                                            text = "${riddle.data?.answer}",
                                            style = MaterialTheme.typography.headlineLarge,
                                            fontWeight = FontWeight.Bold,
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(25.dp))
                        RiddleInputField(
                            otpText = input,
                            otpCount = riddle.data?.answer?.length ?: 6,
                            onOtpTextChange = { input.value = it },
                            error = false
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 25.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TimerButton(key = riddle, onClick = { revealAnswer = true })
                            Button(
                                enabled = input.value.text.isNotBlank(),
                                onClick = onSubmit
                            ) {
                                Text(text = "Submit")
                            }
                        }


                        Button(
                            onClick = {
                                revealAnswer = false
                                input.value = TextFieldValue()
                                data.viewModel.fetchARiddle()
                            }) {
                            if (revealAnswer)
                                Text(text = "Next")
                            else
                                Text(text = "Skip")
                        }
                    }
                }
            }
        }

        AnimatedVisibility(visible = error) {
            Dialog(onDismissRequest = { error = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Sorry! Incorrect Answer",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Button(onClick = { error = false }) {
                            Text(text = "Try again")
                        }
                    }
                }
            }
        }
    }

    AnimatedVisibility(riddle.state is UiState.Loading) {
        LoadingDialog()
    }

    LaunchedEffect(key1 = riddle) {
        if (riddle.data == null && riddle.state !is UiState.Loading)
            data.viewModel.fetchARiddle()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreenTopBar(title: String, enabled: Boolean = true, onBackPressed: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        title = { Text(text = title) },
        navigationIcon = {
            DefaultBackButton(enabled = true, onBackPressed = onBackPressed)
        },
    )
}

@Composable
fun AnimatedIconButton(visibility: Boolean, onClick: () -> Unit, @DrawableRes iconRes: Int) {
    AnimatedVisibility(visibility) {
        IconButton(
            modifier = Modifier.size(23.dp),
            onClick = onClick
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = ""
            )
        }
    }
}

