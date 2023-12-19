package dev.eknath.aiconvo.ui.presentation.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.google.ai.client.generativeai.GenerativeModel
import dev.eknath.aiconvo.ui.presentation.components.DefaultBackButton
import dev.eknath.aiconvo.ui.presentation.components.LoadingOrContentCard
import dev.eknath.aiconvo.ui.presentation.components.RiddleInputField
import dev.eknath.aiconvo.ui.presentation.components.TimerButton
import dev.eknath.aiconvo.ui.presentation.helpers.shareNote
import dev.eknath.aiconvo.ui.presentation.viewmodels.ConvoViewModel


@Stable
data class ScreenParams(
    val navController: NavController,
    val generativeViewModel: GenerativeModel,
    val imageGenerativeModel: GenerativeModel,
    val extraCorrectnessModel: GenerativeModel,
)

@Composable
fun RiddleScreen(data: ScreenParams) {

    val viewModel = remember { ConvoViewModel(data) }

    val context = LocalContext.current
    var score by remember { mutableIntStateOf(0) }
    var input = remember { mutableStateOf(TextFieldValue()) }
    var riddle by remember { viewModel.riddle }
    var revealAnswer by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf(false) }

    val onSubmit = {
        if (input.value.text.lowercase() == riddle?.answer?.lowercase()) {
            score += 1
            revealAnswer = false
            viewModel.fetchARiddle()
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
            Text(text = "Score: ${if (score == 0) "0" else score}")

            LoadingOrContentCard(viewModel.riddle.value != null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
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
                                text = "${riddle?.question}",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 5.dp, bottom = 5.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                AnimatedVisibility(riddle != null) {
                                    IconButton(
                                        modifier = Modifier.size(23.dp),
                                        onClick = {
                                            context.shareNote(
                                                title = "Check out this Riddle: ",
                                                content = riddle?.question.orEmpty()
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
                                        text = "${riddle?.answer}",
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
                        otpCount = riddle?.answer?.length ?: 6,
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


                    Button(onClick = {
                        revealAnswer = false
                        riddle = null
                        input.value = TextFieldValue()
                        viewModel.fetchARiddle()
                    }) {
                        if (revealAnswer)
                            Text(text = "Next")
                        else
                            Text(text = "Skip")
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


    LaunchedEffect(key1 = riddle) {
        if (riddle == null)
            viewModel.fetchARiddle()
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
