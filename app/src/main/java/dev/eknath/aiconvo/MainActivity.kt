package dev.eknath.aiconvo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.ai.client.generativeai.GenerativeModel
import dev.eknath.aiconvo.ui.presentation.screens.RiddleScreen
import dev.eknath.aiconvo.ui.theme.AIConvoTheme
import kotlinx.coroutines.launch

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
                    val generativeModel = GenerativeModel(
                        modelName = AI_MODELS.GEMINI_PRO.key,
                        apiKey = BuildConfig.apiKey
                    )
                    val viewModel = ConvoViewModel(generativeModel)

                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()


                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet(
                                modifier = Modifier.fillMaxWidth(0.5f),
                                content = { DrawerContent() })
                        },
                    ) {
                        Scaffold(
                            modifier = Modifier.padding(),
                            topBar = {
                                CenterAlignedTopAppBar(
                                    title = {
                                        Row {
                                            Text(
                                                text = "AIConvo_",
                                                fontWeight = FontWeight.SemiBold,
                                                style = MaterialTheme.typography.headlineSmall
                                            )
                                        }
                                    },
                                    navigationIcon = {
                                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
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
//                                ChatScreen(viewModel)
                                RiddleScreen(viewModel = viewModel)
                            }
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


@Composable
fun DrawerContent() {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Column {
                Image(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .size(200.dp)
                        .clickable { openUrl(context, "https:eknath.dev") }
                )
                Text(
                    text = "Your AI Companion :)",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-15).dp),
                )
            }
            Divider()
        }

        CreditComponent()
    }
}

@Composable
fun CreditComponent() {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 10.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            Text(
                text = "Made with ",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
            )
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "",
                tint = Color.Red,
                modifier = Modifier.size(15.dp)
            )
        }
        Text(
            text = "Asmakam-AppStudio",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "eknath.dev",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { openUrl(context, "https:eknath.dev") }
        )
    }
}

fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}



