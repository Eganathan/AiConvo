package dev.eknath.aiconvo.ui.presentation.screens

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.eknath.aiconvo.NetworkState
import dev.eknath.aiconvo.SummarizeUiState
import dev.eknath.aiconvo.SummarizeViewModel
import dev.eknath.aiconvo.networkStateProvider
import dev.eknath.aiconvo.ui.presentation.components.ConversationContentUI
import dev.eknath.aiconvo.ui.presentation.components.NetworkErrorDialog

@Composable
internal fun ChatScreen(summarizeViewModel: SummarizeViewModel = viewModel()) {

    val summarizeUiState by summarizeViewModel.covUiData.collectAsState()
    var prompt by remember { mutableStateOf(TextFieldValue()) }

    val isNetWorkAvailable = networkStateProvider()
    val listState = rememberLazyListState()
    val itemCount by remember { derivedStateOf { listState.layoutInfo.totalItemsCount } }


    if (isNetWorkAvailable.value == NetworkState.Disconnected)
        NetworkErrorDialog()

    Box(modifier = Modifier.fillMaxSize()) {
        if (summarizeUiState.isNotEmpty()) {
            LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                items(items = summarizeUiState.sortedBy { it.id }) {
                    ConversationContentUI(it)
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

    //to force scroll when now item is added
    LaunchedEffect(key1 = itemCount) {
        listState.scrollToItem(itemCount)
    }
}