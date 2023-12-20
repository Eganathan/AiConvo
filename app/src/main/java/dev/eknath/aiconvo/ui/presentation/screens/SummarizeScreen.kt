package dev.eknath.aiconvo.ui.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.eknath.aiconvo.ui.presentation.viewmodels.ConvoViewModel

@Composable
fun SummarizeArticle(data: ScreenParams) {
    val viewModel = remember { ConvoViewModel(data) }
    var input by remember { mutableStateOf(TextFieldValue()) }
    val respo = viewModel.summary


    Scaffold(topBar = {
        ActivityScreenTopBar(title = "Summarize Articles",
            enabled = true,
            onBackPressed = { data.navController.navigateUp() })
    }) {

        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Text(
                text = "Paste the link below:",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .padding(top = 5.dp),
                textAlign = TextAlign.Start
            )
            TextField(
                value = input,
                onValueChange = { input = it },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )

            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(horizontal = 5.dp),
                onClick = { viewModel.summarizeArticle(input.text) }) {
                Text(text = "Summarize")
            }

            SelectionContainer {
                Text(
                    text = respo.value.orEmpty().toAnnotatedString(),
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState()),
                )
            }

        }
    }
}

fun String.toAnnotatedString(): AnnotatedString {
    val parts = this.split("**")
    val annotatedString = AnnotatedString.Builder()

    parts.forEachIndexed { index, part ->
        if (index % 2 == 0) {
            annotatedString.append(part)
        } else {
            annotatedString.pushStyle(SpanStyle(fontWeight = FontWeight.ExtraBold))
            annotatedString.append(part)
            annotatedString.pop()
        }
    }

    return annotatedString.toAnnotatedString()
}