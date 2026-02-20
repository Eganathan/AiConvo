package dev.eknath.aiconvo.presentation.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.eknath.aiconvo.presentation.presentation.components.NetworkErrorDialog
import dev.eknath.aiconvo.presentation.presentation.helpers.NetworkState
import dev.eknath.aiconvo.presentation.presentation.helpers.networkStateProvider
import dev.eknath.aiconvo.presentation.presentation.screens.ChatScreen
import dev.eknath.aiconvo.presentation.presentation.screens.HomeScreen
import dev.eknath.aiconvo.presentation.presentation.screens.MathChallengeScreen
import dev.eknath.aiconvo.presentation.presentation.screens.NewsScreen
import dev.eknath.aiconvo.presentation.presentation.screens.RiddleScreen
import dev.eknath.aiconvo.presentation.presentation.screens.ScreenParams
import dev.eknath.aiconvo.presentation.presentation.screens.SummarizeArticle
import dev.eknath.aiconvo.presentation.presentation.viewmodels.ConvoViewModel
import dev.eknath.aiconvo.presentation.presentation.viewmodels.RiddleViewModel
import dev.eknath.aiconvo.presentation.presentation.viewmodels.RiddleViewModelFactory

enum class ROUTES {
    HOME, CHAT, RIDDLES, MATH_CHALLENGE, TECH_NEWS, SUMMARIZE, TRUTH_OR_DARE,
}

@Composable
fun AppNavigation(apiKey: String) {

    val koogHelper = remember { KoogHelper(apiKey) }

    val isNetWorkAvailable = networkStateProvider()

    val navController = rememberNavController()
    val parameters = remember {
        ScreenParams(
            navController = navController,
            viewModel = ConvoViewModel(koogHelper = koogHelper)
        )
    }


    if (isNetWorkAvailable.value == NetworkState.Disconnected)
        NetworkErrorDialog()

    NavHost(navController = navController, startDestination = ROUTES.HOME.name) {
        composable(route = ROUTES.HOME.name, content = { HomeScreen(data = parameters) })
        composable(route = ROUTES.CHAT.name, content = { ChatScreen(data = parameters) })
        composable(route = ROUTES.RIDDLES.name, content = {
            //Riddle ViewModel
            val factory = RiddleViewModelFactory(koogHelper)
            val riddleViewModel =
                viewModel(modelClass = RiddleViewModel::class.java, factory = factory)

            RiddleScreen(navController = navController, viewModel = riddleViewModel)
        })
        composable(
            route = ROUTES.MATH_CHALLENGE.name,
            content = { MathChallengeScreen(data = parameters) })
        composable(route = ROUTES.TECH_NEWS.name, content = { NewsScreen(data = parameters) })
        composable(route = ROUTES.SUMMARIZE.name, content = { SummarizeArticle(data = parameters) })
    }

}
