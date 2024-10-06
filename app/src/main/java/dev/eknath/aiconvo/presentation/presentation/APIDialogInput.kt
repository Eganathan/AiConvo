package dev.eknath.aiconvo.presentation.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

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
            ) {
                var textState by remember { mutableStateOf(TextFieldValue()) }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Enter Your Key",
                        style = MaterialTheme.typography.titleLarge
                    )

                    // TextField for input
                    OutlinedTextField(
                        value = textState,
                        onValueChange = { newText -> textState = newText },
                        label = { Text("Input Key") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Submit button
                    Button(
                        onClick = {
                            if (textState.text.isNotEmpty()) {
                                onSubmit(textState.text)
                                onDismiss() // Close dialog after submitting
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}
