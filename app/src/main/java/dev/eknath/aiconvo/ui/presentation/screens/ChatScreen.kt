package dev.eknath.aiconvo.ui.presentation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import dev.eknath.aiconvo.ACTIVITY
import dev.eknath.aiconvo.ConvoViewModel
import dev.eknath.aiconvo.NetworkState
import dev.eknath.aiconvo.SummarizeUiState
import dev.eknath.aiconvo.networkStateProvider
import dev.eknath.aiconvo.ui.presentation.components.ActivitiesOptions
import dev.eknath.aiconvo.ui.presentation.components.ConversationContentUI
import dev.eknath.aiconvo.ui.presentation.components.NetworkErrorDialog
import dev.eknath.aiconvo.ui.presentation.components.QuoteCard

@Composable
internal fun ChatScreen(summarizeViewModel: ConvoViewModel) {

    val chatContent by summarizeViewModel.covUiData.collectAsState()
    var promt by remember { mutableStateOf(TextFieldValue()) }
    val isNetWorkAvailable = networkStateProvider()
    val listState = rememberLazyListState()


    var activity by remember { mutableStateOf(ACTIVITY.NONE) }


    if (isNetWorkAvailable.value == NetworkState.Disconnected)
        NetworkErrorDialog()

    Box(modifier = Modifier.fillMaxSize()) {
        if (chatContent.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                verticalArrangement = Arrangement.Bottom
            ) {
                items(items = chatContent.sortedBy { it.id }) {
                    ConversationContentUI(it)
                }
                item { Spacer(modifier = Modifier.height(60.dp)) }

            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {

                // Prompt based Activities //todo make this navigate
                when (activity) {
                    ACTIVITY.RIDDLE -> Box(modifier = Modifier.fillMaxSize()) {
                        RiddleScreen(viewModel = summarizeViewModel)
                    }

                    else -> Box(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .fillMaxWidth()
                        ) {
                            ActivitiesOptions(onClickAction = { activity = it })
                        }

                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            QuoteCard( //todo add a share,reload and save options
                                summarizeViewModel.techQuote.value?.quote,
                                summarizeViewModel.techQuote.value?.author
                            )
                            Divider(modifier = Modifier.fillMaxWidth(0.6f))
                            Spacer(modifier = Modifier.height(25.dp))
                            Button(onClick = { summarizeViewModel.generateContent("Hello!") }) {
                                Text(text = "Start with a Hello?")
                            }

                        }
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
                    .padding(horizontal = 2.dp, vertical = 2.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(5.dp)
                    )
            ) {
                TextField(
                    enabled = chatContent.lastOrNull()?.state != SummarizeUiState.Loading,
                    value = promt,
                    placeholder = { Text(text = "Please type your question here...") },
                    onValueChange = { promt = it },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        autoCorrect = true
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        if (promt.text.isNotBlank() && chatContent.lastOrNull()?.state != SummarizeUiState.Loading) {
                            summarizeViewModel::generateContent.invoke(promt.text)
                            promt = TextFieldValue()
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
                                enabled = (promt.text.isNotBlank() && chatContent.lastOrNull()?.state != SummarizeUiState.Loading),
                                onClick = {
                                    summarizeViewModel::generateContent.invoke(promt.text)
                                    promt = TextFieldValue("")

                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Send,
                                    contentDescription = ""
                                )
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    )
                )
            }
        }

        LaunchedEffect(key1 = chatContent) {
            listState.scrollToItem((chatContent.size))
        }
    }
}
