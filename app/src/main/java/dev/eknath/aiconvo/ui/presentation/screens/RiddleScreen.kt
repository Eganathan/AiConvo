package dev.eknath.aiconvo.ui.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import dev.eknath.aiconvo.ConvoViewModel


@Composable
fun RiddleScreen(viewModel: ConvoViewModel) {
    var score by remember { mutableIntStateOf(0) }
    var input by remember { mutableStateOf(TextFieldValue()) }
    var riddle by remember { viewModel.riddle }


    LoadingOrContentCard(viewModel.riddle.value != null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Score: ${if (score == 0) "-" else score}")

            Card {
                Text(text = "Question:")
                Text(text = "${riddle?.question}")

                Text(text = "Answer:")
                Text(text = "${riddle?.answer}")
            }

            TextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text(text = "Type your answer") })

            Button(
                enabled = input.text.isNotBlank(),
                onClick = {
                    if (input.text.lowercase() == riddle?.answer?.lowercase()) {
                        score += 1
                        viewModel.fetchARiddle()
                        input = TextFieldValue()
                    }


                }) {
                Text(text = "Submit")
            }

            Button(onClick = {
                viewModel.fetchARiddle()
                input = TextFieldValue()
            }) {
                Text(text = "Skip")
            }
        }
    }


    LaunchedEffect(key1 = Unit) {
        viewModel.fetchARiddle()
    }

}