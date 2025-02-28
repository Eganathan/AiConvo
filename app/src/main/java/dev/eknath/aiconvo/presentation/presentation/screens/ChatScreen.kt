package dev.eknath.aiconvo.presentation.presentation.screens

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.eknath.aiconvo.presentation.presentation.components.ConversationContentUI
import dev.eknath.aiconvo.presentation.presentation.components.QuoteCard
import dev.eknath.aiconvo.presentation.presentation.states.UiState
import dev.eknath.aiconvo.presentation.presentation.viewmodels.ConvoViewModel

//TODO("Remove this Screen Params")
@Stable
data class ScreenParams(val navController: NavController, val viewModel: ConvoViewModel)

@Composable
internal fun ChatScreen(data: ScreenParams) {

    val chatContent by data.viewModel.covUiData.collectAsState()
    var promt by remember { mutableStateOf(TextFieldValue()) }
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            ActivityScreenTopBar(
                title = "AI-Covo",
                enabled = true,
                onBackPressed = { data.navController.navigateUp() })
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
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
//            Row { ActivitiesOptions({ data.navController.navigate(route = it.routes.name) }) }
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    QuoteCard( //todo add a share,reload and save options
                        data.viewModel.techQuote.value?.quote,
                        data.viewModel.techQuote.value?.author
                    )

                    Divider(modifier = Modifier.fillMaxWidth(0.6f))
                    Spacer(modifier = Modifier.height(25.dp))
                    Button(onClick = { data.viewModel.generateContent("Hello!") }) {
                        Text(text = "Start with a Hello?")
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
                        enabled = chatContent.lastOrNull()?.state != UiState.Loading,
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
                            if (promt.text.isNotBlank() && chatContent.lastOrNull()?.state != UiState.Loading) {
                                data.viewModel::generateContent.invoke(promt.text)
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
                                    enabled = (promt.text.isNotBlank() && chatContent.lastOrNull()?.state != UiState.Loading),
                                    onClick = {
                                        data.viewModel::generateContent.invoke(promt.text)
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
        }

        LaunchedEffect(key1 = chatContent) {
            listState.scrollToItem((chatContent.size))
        }

        LaunchedEffect(key1 = data.viewModel.techQuote) {
            if (data.viewModel.techQuote.value == null)
                data.viewModel.techQuote
        }
    }
}