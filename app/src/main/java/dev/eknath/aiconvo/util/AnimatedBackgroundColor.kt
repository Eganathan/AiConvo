package dev.eknath.aiconvo.util

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape

@Composable
fun Modifier.animatedGradientBackground(shape: Shape = RectangleShape): Modifier {
    // Define a transition that will continuously change over time
    val infiniteTransition = rememberInfiniteTransition(label = "ai_convo_background_color")

    // Animate multiple colors for the gradient
    val color1 by infiniteTransition.animateColor(
        initialValue = Color(0xFFee7752), // #ee7752
        targetValue = Color(0xFF23d5ab), // #23d5ab
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "ai_convo_background_color_1"
    )

    val color2 by infiniteTransition.animateColor(
        initialValue = Color(0xFFe73c7e), // #e73c7e
        targetValue = Color(0xFF23a6d5), // #23a6d5
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "ai_convo_background_color_2"
    )

    val color3 by infiniteTransition.animateColor(
        initialValue = Color(0xFF23a6d5), // #23a6d5
        targetValue = Color(0xFFe73c7e), // #e73c7e
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ai_convo_background_color_3"
    )

    val color4 by infiniteTransition.animateColor(
        initialValue = Color(0xFF23d5ab), // #23d5ab
        targetValue = Color(0xFFee7752), // #ee7752
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ai_convo_background_color_4"
    )

    // Apply the animated gradient background as a modifier
    return this.background(
        shape = shape,
        brush = Brush.linearGradient(
            colors = listOf(color1, color2, color3, color4),
            start = Offset.Zero, // Starting point of the gradient
            end = Offset.Infinite // You can adjust this to simulate the -45 degree angle
        )
    )
}
