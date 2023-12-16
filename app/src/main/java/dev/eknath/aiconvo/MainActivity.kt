package dev.eknath.aiconvo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.google.ai.client.generativeai.GenerativeModel
import dev.eknath.aiconvo.ui.presentation.screens.ChatScreen
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
                    val generativeModel = GenerativeModel(modelName = AI_MODELS.GEMINI_PRO.key, apiKey = BuildConfig.apiKey)
                    val viewModel = ConvoViewModel(generativeModel)

                    Scaffold(
                        modifier = Modifier.padding(),
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Row {
                                        Text(
                                            text = "AIConvo_",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.headlineMedium
                                        )
                                    }
                                },
                                navigationIcon = {
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(
                                            imageVector = Icons.Filled.Menu,
                                            contentDescription = ""
                                        )
                                    }
                                },
                                actions = {
                                    IconButton(onClick = { }) {
                                        Icon(
                                            imageVector = Icons.Filled.Settings,
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
                            ChatScreen(viewModel)
                        }
                    }
                }
            }
        }
    }
}

enum class AI_MODELS(val key: String) {
    GEMINI_PRO(key = "gemini-pro"), GEMINI_PRO_VISION(key = "gemini-pro-vision")
}

@Composable
fun SettingSheetComponent(viewmodel: ConvoViewModel) {
    Column {

    }
}



