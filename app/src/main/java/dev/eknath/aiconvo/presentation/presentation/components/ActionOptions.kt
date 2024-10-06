package dev.eknath.aiconvo.presentation.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.eknath.aiconvo.R
import dev.eknath.aiconvo.presentation.enums.PROMPT_ACTIVITY

@Composable
fun ActivitiesOptions(onClickAction: (PROMPT_ACTIVITY) -> Unit) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .padding(top = 5.dp)
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_game),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(10.dp))

        listOf(
            PROMPT_ACTIVITY.RIDDLE,
            PROMPT_ACTIVITY.MATH_CHALLENGE,
            PROMPT_ACTIVITY.SUMMARIZE_ARTICLE
        ).forEach {
            Button(
                onClick = { onClickAction(it) }) {
                Text(text = it.name.replace("_", " "))
            }
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .size(7.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary)
            ) {}
        }
    }
}
