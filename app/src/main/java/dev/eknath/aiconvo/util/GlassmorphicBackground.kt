package dev.eknath.aiconvo.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

fun Modifier.glassMorphicBackground(): Modifier {
    return this
        .graphicsLayer {
            // Adding a blur effect for the frosted glass look
            alpha = 0.9f
            shadowElevation = 8.dp.toPx()
            shape = RoundedCornerShape(16.dp)
            clip = true
        }
        // Add a background with semi-transparent color or gradient
        .background(
            brush = Brush.linearGradient(
                listOf(
                    Color.White.copy(alpha = 0.1f), // Semi-transparent white
                    Color.White.copy(alpha = 0.3f)
                )
            )
        )
        // Apply blur effect for glass-like appearance
        .blur(0.1.dp)
        .border(
            width = 1.dp,
            color = Color.White.copy(alpha = 0.2f),
            shape = RoundedCornerShape(16.dp)
        )
}

