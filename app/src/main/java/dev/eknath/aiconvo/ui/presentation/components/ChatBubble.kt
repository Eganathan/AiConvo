package dev.eknath.aiconvo.ui.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.eknath.aiconvo.R
import dev.eknath.aiconvo.ui.presentation.helpers.shareNote
import dev.eknath.aiconvo.ui.presentation.states.UiState
import dev.eknath.aiconvo.ui.presentation.viewmodels.Conv
import dev.eknath.aiconvo.ui.presentation.viewmodels.Owner

@Composable
fun ConversationBubble(
    message: String,
    sender: Boolean
) {
    val content = LocalContext.current
    val cardShape =
        RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp, bottomStart = 15.dp, bottomEnd = 15.dp)

    Column {
        if (!sender)
            BotHeaderContent(onShare = { content::shareNote.invoke("", message) })
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(5.dp)
                .then(if (sender) Modifier.padding(start = 20.dp) else Modifier.padding(end = 20.dp)),
            colors =
            CardDefaults.cardColors(containerColor = if (sender) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer),
            shape = if (sender) cardShape.copy(bottomEnd = CornerSize(0.dp)) else cardShape.copy(
                topStart = CornerSize(0.dp)
            )
        ) {
            Text(
                text = message,
                modifier = Modifier
                    .defaultMinSize(minWidth = 50.dp)
                    .padding(5.dp)
            )
        }
    }
}

@Composable
fun ConversationContentUI(input: Conv) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (input.owner == Owner.AI) Arrangement.Start else Arrangement.End
    ) {
        if (input.state is UiState.Error) {
            Row(verticalAlignment = Alignment.Bottom) {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = "",
                    tint = Color.Red,
                    modifier = Modifier.size(25.dp)
                )
                Text(text = "Sorry,faced some error!", style = MaterialTheme.typography.bodySmall)
            }
        } else if (input.state is UiState.Loading) {
            BubbleLoading(true)
        } else if (input.state is UiState.Success) {
            ConversationBubble(message = input.value, sender = (input.owner == Owner.USER))
        }
    }
}

@Composable
private fun BotHeaderContent(onShare: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(start = 5.dp)
            .offset(y = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_bot),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(20.dp)
        )
        IconButton(
            modifier = Modifier
                .padding(start = 5.dp)
                .size(15.dp),
            onClick = onShare
        ) {
            Icon(imageVector = Icons.Rounded.Share, contentDescription = "")
        }
    }
}