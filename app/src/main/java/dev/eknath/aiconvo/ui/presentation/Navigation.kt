package dev.eknath.aiconvo.ui.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.ai.client.generativeai.GenerativeModel
import dev.eknath.aiconvo.BuildConfig
import dev.eknath.aiconvo.ui.enums.AI_MODELS
import dev.eknath.aiconvo.ui.presentation.components.NetworkErrorDialog
import dev.eknath.aiconvo.ui.presentation.helpers.NetworkState
import dev.eknath.aiconvo.ui.presentation.helpers.networkStateProvider
import dev.eknath.aiconvo.ui.presentation.screens.ChatScreen
import dev.eknath.aiconvo.ui.presentation.screens.HomeScreen
import dev.eknath.aiconvo.ui.presentation.screens.MathChallengeScreen
import dev.eknath.aiconvo.ui.presentation.screens.RiddleScreen
import dev.eknath.aiconvo.ui.presentation.screens.ScreenParams

enum class ROUTES {
    HOME, CHAT, RIDDLES, MATH_CHALLENGE, OTHER
}

@Composable
fun Application() {
    val generativeModel = remember {
        GenerativeModel(
            modelName = AI_MODELS.GEMINI_PRO.key,
            apiKey = BuildConfig.apiKey
        )
    }

    val isNetWorkAvailable = networkStateProvider()

    val navController = rememberNavController()
    val parameters = remember { ScreenParams(navController = navController, generativeModel) }

    if (isNetWorkAvailable.value == NetworkState.Disconnected)
        NetworkErrorDialog()

    NavHost(navController = navController, startDestination = ROUTES.HOME.name) {
        composable(route = ROUTES.HOME.name, content = { HomeScreen(data = parameters) })
        composable(route = ROUTES.CHAT.name, content = { ChatScreen(data = parameters) })
        composable(route = ROUTES.RIDDLES.name, content = { RiddleScreen(data = parameters) })
        composable(
            route = ROUTES.MATH_CHALLENGE.name,
            content = { MathChallengeScreen(data = parameters) })
    }


}