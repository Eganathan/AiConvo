package dev.eknath.aiconvo.presentation.presentation.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.collectAsState
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
import dev.eknath.aiconvo.presentation.presentation.states.UiState

@Composable
fun SummarizeArticle(data: ScreenParams) {
    var input by remember { mutableStateOf(TextFieldValue()) }
    val summary by  data.viewModel.summary.collectAsState()


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
                onClick = {  data.viewModel.summarizeArticle(input.text) }) {
                Text(text = "Summarize")
            }

            SelectionContainer {
                Text(
                    text = summary.value?.toAnnotatedString() ?: AnnotatedString(""),
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState()),
                )
            }

        }
    }

    AnimatedVisibility(summary.state is UiState.Loading) {
        LoadingDialog()
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