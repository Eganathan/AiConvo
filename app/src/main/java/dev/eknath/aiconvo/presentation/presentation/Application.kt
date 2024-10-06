package dev.eknath.aiconvo.presentation.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dev.eknath.aiconvo.BuildConfig
import dev.eknath.aiconvo.util.getApiKeyOrDefault
import dev.eknath.aiconvo.util.setApiKey

@Composable
fun Application(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val prefApiKey = (context.getApiKeyOrDefault() ?: BuildConfig.apiKey)
    val apiKey = remember { mutableStateOf(prefApiKey) }

    val showDialog = remember { derivedStateOf { apiKey.value.length < 25 } }

    if (showDialog.value)
        APIKeyDialog(
            showDialog = true,
            onDismiss = {},
            onSubmit = {
                apiKey.value = it
                context.setApiKey(apiKey.value)
            }
        )
    else
        AppNavigation(apiKey.value)
}

