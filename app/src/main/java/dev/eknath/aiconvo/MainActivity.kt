package dev.eknath.aiconvo

import Loading
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Fill
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultBlendMode
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.ai.client.generativeai.GenerativeModel
import dev.eknath.aiconvo.ui.theme.AIConvoTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
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

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text(text = "AIConvo") },
                                navigationIcon = {
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(
                                            imageVector = Icons.Filled.Menu,
                                            contentDescription = ""
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    ) {
                        Box(modifier = Modifier.padding(it)) {
                            ConversationScreen(viewModel)
                        }
                    }
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
    val isNetWorkAvailable = networkStateProvider()

    if (isNetWorkAvailable.value == NetworkState.Disconnected)
        NetworkErrorDialog()

    Box(modifier = Modifier.fillMaxSize()) {
        if (summarizeUiState.isNotEmpty()) {
            LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                items(items = summarizeUiState.sortedBy { it.id }) {
                    ConversationUI(it)
                }
                item { Spacer(modifier = Modifier.height(60.dp)) }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Hi! Do you have any questions fo me?")
                    Button(onClick = { summarizeViewModel.summarize("Hello!") }) {
                        Text(text = "or Just Hello!")
                    }

                }

            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 2.dp)
            ) {
                TextField(
                    enabled = summarizeUiState.lastOrNull()?.state != SummarizeUiState.Loading,
                    value = prompt,
                    onValueChange = { prompt = it },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        autoCorrect = true
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        if (prompt.text.isNotEmpty() && summarizeUiState.lastOrNull()?.state != SummarizeUiState.Loading) {
                            summarizeViewModel::summarize.invoke(prompt.text)
                            prompt = TextFieldValue()
                        }
                    }),
                    trailingIcon = {
                        Box {
                            Button(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .wrapContentSize()
                                    .padding(end = 5.dp),
                                shape = RoundedCornerShape(5.dp),
                                enabled = (prompt.text.isNotEmpty() && summarizeUiState.lastOrNull()?.state != SummarizeUiState.Loading),
                                onClick = {
                                    summarizeViewModel::summarize.invoke(prompt.text)
                                    prompt = TextFieldValue("")

                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Send,
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                )
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
            Row(verticalAlignment = Alignment.Bottom) {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = "",
                    tint = Color.Red,
                    modifier = Modifier.size(25.dp)
                )
                Text(text = "Sorry,faced some error!", style = MaterialTheme.typography.bodySmall)
            }
        } else if (input.state is SummarizeUiState.Loading) {
            Loading(true)
        } else if (input.state is SummarizeUiState.Success) {

            ConversationBubble(message = input.value, sender = (input.owner == Owner.USER))
        }
    }
}

@Composable
fun NetworkErrorDialog() {
    Dialog(
        onDismissRequest = {},
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.9f)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = "",
                tint = Color.Red,
                modifier = Modifier.size(50.dp)
            )
            Text(text = "Network Issue!", style = MaterialTheme.typography.headlineSmall)
            Loading(visibility = true)
        }
    }
}

@Composable
fun ConversationBubble(
    message: String,
    sender: Boolean
) {
    val cardShape =
        RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp, bottomStart = 15.dp, bottomEnd = 15.dp)
    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(5.dp)
            .padding(top = 5.dp),
        colors =
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        shape = if (sender) cardShape.copy(bottomEnd = CornerSize(0.dp)) else cardShape.copy(
            topStart = CornerSize(0.dp)
        )
    ) {
        Text(
            text = message,
            modifier = Modifier
                .defaultMinSize(minWidth = 50.dp)
                .padding(5.dp)
        )
    }
}



