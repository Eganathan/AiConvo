package dev.eknath.aiconvo.ui.presentation.screens

import android.util.Log
import androidx.activity.compose.BackHandler
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
import dev.eknath.aiconvo.ACTIVITY
import dev.eknath.aiconvo.ConvoViewModel
import dev.eknath.aiconvo.ui.presentation.components.LoadingOrContentCard

@Composable
fun MathChallengeScreen(
    viewModel: ConvoViewModel,
    activeActivity: ACTIVITY,
    onActivitySelected: (ACTIVITY) -> Unit
) {

    val onBackPressed = { onActivitySelected.invoke(ACTIVITY.NONE) }
    val challenge by remember { derivedStateOf { viewModel.mathChallenge.value } }

    Scaffold(
        topBar = {
            ActivityScreenTopBar(
                title = "Math Challenge",
                enabled = true,
                onBackPressed = onBackPressed
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
    BackHandler(enabled = true) {
        onBackPressed()
    }

    LaunchedEffect(challenge) {
        if (challenge == null && activeActivity == ACTIVITY.MATH_PROBLEM) {
            viewModel.fetchMathChallenge()
            Log.e("Test","Fired!")
        }
    }
}