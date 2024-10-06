package dev.eknath.aiconvo.presentation.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun DefaultBackButton(enabled: Boolean, onBackPressed: () -> Unit) {
    IconButton(enabled = enabled, onClick = onBackPressed) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
    }
}