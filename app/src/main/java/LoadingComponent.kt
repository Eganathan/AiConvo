import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun Loading(visibility: Boolean) {
    AnimatedVisibility(visible = visibility) {
        val maxProgress = 2
        val animationDuration = 150
        var index by remember { mutableIntStateOf(0) }

        Row(
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 5.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            repeat(maxProgress + 1) {
                Spacer(modifier = Modifier.width(3.dp))
                ProgressIndicator(isActive = index == it)
            }
        }

        LaunchedEffect(key1 = index) {
            while (true) {
                delay(animationDuration.toLong())
                index = (index + 1) % (maxProgress + 1)
            }
        }
    }
}

@Composable
fun ProgressIndicator(isActive: Boolean) {
    val activeSize = 15.dp
    val inactiveSize = 12.dp
    val padding = 2.5.dp
    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.secondary

    val size = if (isActive) activeSize else inactiveSize
    val color = if (isActive) activeColor else inactiveColor

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
            .padding(padding)
    )
}