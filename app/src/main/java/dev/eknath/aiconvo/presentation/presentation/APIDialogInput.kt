package dev.eknath.aiconvo.presentation.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.eknath.aiconvo.util.animatedGradientBackground

@Composable
fun APIKeyDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(16.dp)
                    .border(3.dp, color = Color.Transparent)
            ) {
                var textState by remember { mutableStateOf(TextFieldValue()) }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Gemini API Key",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp)
                    )

                    Text(
                        text = "Paste you api key from https://aistudio.google.com/app/apikey",
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)

                    )

                    // TextField for input
                    OutlinedTextField(
                        value = textState,
                        onValueChange = { newText -> textState = newText },
                        label = { Text("Your API Key") },
                        placeholder = {
                            Text("You Gemini API Key")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    )

                    Text(
                        text = "* please ensure the key is valid otherwise the app will not work as expected, you can also update the key from settings.",
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                    // Submit button
                    Text(
                        text = "Proceed",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = if (textState.text.length < 25) 0.6f else 1f),
                        fontSize = 24.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 20.dp)
                            .padding(vertical = 2.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .animatedGradientBackground(RoundedCornerShape(10.dp))
                            .clickable {
                                if (textState.text.length > 25)
                                    onSubmit(textState.text)
                            }
                    )

                }
            }
        }
    }
}
