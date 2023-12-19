package dev.eknath.aiconvo.ui.presentation.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import dev.eknath.aiconvo.ui.presentation.components.BubbleLoading
import dev.eknath.aiconvo.ui.presentation.helpers.openUrl
import dev.eknath.aiconvo.ui.presentation.states.UiState
import dev.eknath.aiconvo.ui.presentation.viewmodels.ConvoViewModel


@Composable
internal fun NewsScreen(data: ScreenParams) {

    val viewModel = remember { ConvoViewModel(data.generativeViewModel) }
    val news by viewModel.news.collectAsState()

    Scaffold(topBar = {
        ActivityScreenTopBar(title = "Now in Tech and Science",
            enabled = true,
            onBackPressed = { data.navController.navigateUp() })
    }) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (news.state) {
                UiState.Error -> Text(
                    text = "Error, click to try again",
                    modifier = Modifier.clickable { viewModel.fetchNews() })

                UiState.Loading -> BubbleLoading(visibility = true)
                UiState.Initial -> BubbleLoading(visibility = true)
                UiState.Success -> LazyColumn(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {
                    items(items = news.value?.news.orEmpty()) {
                        SimpleNewsCard(
                            title = it.headline,
                            summary = it.summary,
                            link = it.link,
                            imageUrl = it.imageLink
                        )
                    }
                }

                UiState.Initial -> TODO()
            }
        }

    }

    LaunchedEffect(news.value) {
        if (news.value?.news.orEmpty().isEmpty() && news.state != UiState.Loading) {
            viewModel.fetchNews()
            Log.e("Test", "Fired!")
        }
    }
}

@Composable
fun SimpleNewsCard(title: String, summary: String, link: String, imageUrl: String?) {
    val context = LocalContext.current

    Card(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(text = title)
            Text(text = summary)
            Text(
                text = "read more",
                style = MaterialTheme.typography.bodySmall,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { openUrl(context = context, url = link) })
        }
        Divider()
    }

}