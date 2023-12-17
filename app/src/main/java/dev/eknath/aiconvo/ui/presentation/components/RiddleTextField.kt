package dev.eknath.aiconvo.ui.presentation.components


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RiddleInputField(
    modifier: Modifier = Modifier,
    otpText: State<TextFieldValue>,
    otpCount: Int,
    onOtpTextChange: (TextFieldValue) -> Unit,
    error: Boolean = true
) {

    BasicTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = otpText.value,
        onValueChange = onOtpTextChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        decorationBox = {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(otpCount) { index ->
                    CharView(
                        index = index,
                        text = otpText.value.text,
                        isCursorVisible = index == otpText.value.text.length,
                        isError = error
                    )

                }
            }
        })

    AnimatedVisibility(visible = error) {
        Text(
            text = "Sorry! Please Try again...",
            modifier = Modifier.padding(top = 10.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}


@Composable
private fun CharView(
    index: Int,
    text: String,
    isCursorVisible: Boolean,
    isError: Boolean
) {
    val scope = rememberCoroutineScope()
    val (cursorSymbol, setCursorSymbol) = remember { mutableStateOf("") }

    LaunchedEffect(key1 = cursorSymbol, isCursorVisible) {
        if (isCursorVisible) {
            scope.launch {
                delay(300)
                setCursorSymbol(if (cursorSymbol.isEmpty()) "|" else "")
            }
        }
    }

    val char = when {
        index == text.length -> ""
        index > text.length -> ""
        else -> text[index].toString()
    }
    Column(
        modifier = Modifier
            .padding(end = 5.dp)
            .defaultMinSize(minWidth = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicText(
            text = if (isCursorVisible) cursorSymbol else char.uppercase(),
            style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.primary),
            modifier = Modifier.padding(bottom = 2.8.dp),
        )
        Divider(
            thickness = 3.dp,
            modifier = Modifier.width(50.dp),
            color = (if (char.isNotBlank() && !isCursorVisible) MaterialTheme.colorScheme.primary else if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.inversePrimary)
        )
    }

}