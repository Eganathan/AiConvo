package dev.eknath.aiconvo.ui.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.eknath.aiconvo.ui.presentation.viewmodels.ConvoViewModel
import dev.eknath.aiconvo.ui.presentation.components.LoadingOrContentCard


@Composable
internal fun MathChallengeScreen(data: ScreenParams) {

    val viewModel = ConvoViewModel(data.generativeViewModel)
    val challenge by remember { derivedStateOf { viewModel.mathChallenge.value } }
    Scaffold(
        topBar = {
            ActivityScreenTopBar(
                title = "Math Challenge",
                enabled = true,
                onBackPressed = { data.navController.navigateUp() }
            )
        }
    ) {
        Column(Modifier.padding(it)) {
            LoadingOrContentCard(isLoading = challenge == null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(5.dp)
                            .padding(start = 5.dp)
                    ) {
                        Text(
                            text = challenge?.question ?: "",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }

            }
        }
    }

    LaunchedEffect(challenge) {
        if (challenge == null) {
            viewModel.fetchMathChallenge()
            Log.e("Test", "Fired!")
        }
    }
}