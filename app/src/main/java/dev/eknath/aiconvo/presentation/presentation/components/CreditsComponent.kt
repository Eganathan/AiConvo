package dev.eknath.aiconvo.presentation.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.eknath.aiconvo.presentation.presentation.helpers.openUrl

@Composable
private fun CreditComponent() {
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