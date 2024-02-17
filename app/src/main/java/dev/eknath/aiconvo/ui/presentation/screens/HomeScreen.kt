@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package dev.eknath.aiconvo.ui.presentation.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.eknath.aiconvo.R
import dev.eknath.aiconvo.ui.enums.PROMPT_ACTIVITY
import dev.eknath.aiconvo.ui.presentation.ROUTES
import dev.eknath.aiconvo.ui.presentation.helpers.openUrl
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(data: ScreenParams) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.5f),
                content = { DrawerContent(navController = data.navController) })
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
//                ChatScreen(data)
                MainScreen()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DrawerContent(navController: NavController) {
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
                )
                Text(
                    text = "Your AI Companion :)",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-15).dp)
                        .clickable { openUrl(context, "https:eknath.dev") },
                )
            }

            Divider(modifier = Modifier.padding(vertical = 5.dp))


//            val currRoute = navController.visibleEntries.collectAsState().value
//            val currentRoute by remember{ derivedStateOf { currRoute.firstOrNull()?.destination?.route.orEmpty() }}
            Column {

                ROUTES.entries.filter { it != ROUTES.CHAT && ROUTES.TECH_NEWS != it }.forEach {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 40.dp)
                            .padding(0.dp),
                        onClick = { navController.navigate(it.name) }) {
                        Text(
                            text = it.name,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = 10.dp)
                        )
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    LazyColumn {
        items(PROMPT_ACTIVITY.entries) {
            HomeListItem(title = it.title, icon = it.iconRes, onClick = {})
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun HomeListItem(@DrawableRes icon: Int, title: String, onClick: () -> Unit) {
    val defaultMinHeight = 100.dp
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .height(defaultMinHeight)
            .fillMaxWidth(),
        onClick = { /*TODO*/ }) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .size(defaultMinHeight)
                    .aspectRatio(1f)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "",
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .size(defaultMinHeight / 2)
                        .align(Alignment.Center)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.offset(y = 30.dp, x = 15.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}