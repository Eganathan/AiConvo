package dev.eknath.aiconvo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.BuildCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.ai.client.generativeai.GenerativeModel
import dev.eknath.aiconvo.ui.theme.AIConvoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIConvoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val generativeModel =
                        GenerativeModel(modelName = "gemini-pro", apiKey = BuildConfig.apiKey)
                    val viewModel = SummarizeViewModel(generativeModel)

                    ConversationScreen(viewModel)
                }
            }
        }
    }
}

@Composable
internal fun ConversationScreen(
    summarizeViewModel: SummarizeViewModel = viewModel()
) {
    val summarizeUiState by summarizeViewModel.covUiData.collectAsState()
    var prompt by remember { mutableStateOf(TextFieldValue()) }

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(Modifier.fillMaxSize()) {
            items(items = summarizeUiState) {
                ConversationUI(it)
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxSize()
                .padding(bottom = 20.dp)
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .defaultMinSize(minHeight = 100.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 10.dp)
            ) {
                TextField(
                    enabled = summarizeUiState.lastOrNull()?.state != SummarizeUiState.Loading,
                    value = prompt,
                    onValueChange = { prompt = it },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        autoCorrect = true
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        if (prompt.text.isNotEmpty() && summarizeUiState.lastOrNull()?.state != SummarizeUiState.Loading) {
                            summarizeViewModel::summarize.invoke(prompt.text)
                            prompt = TextFieldValue()
                        }
                    })
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding(),
                    shape = RoundedCornerShape(5.dp),
                    enabled = (prompt.text.isNotEmpty() && summarizeUiState.lastOrNull()?.state != SummarizeUiState.Loading),
                    onClick = {
                        summarizeViewModel::summarize.invoke(prompt.text)
                        prompt = TextFieldValue()

                    }
                ) {
                    Text(text = "Send")
                }
            }
        }
    }

}

@Composable
fun ConversationUI(input: Conv) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (input.owner == Owner.AI) Arrangement.Start else Arrangement.End
    ) {
        if (input.state is SummarizeUiState.Error) {
            Icon(
                imageVector = Icons.Rounded.Warning,
                contentDescription = "",
                tint = Color.Red
            )
        } else if (input.state is SummarizeUiState.Loading) {
            Text(text = "Loading...")
        } else if (input.state is SummarizeUiState.Success) {
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(5.dp)
                    .padding(top = 10.dp),
                colors =
                CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(text = input.value)
            }
        }
    }
}
