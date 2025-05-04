package ru.itis.application7.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.itis.application7.core.ui.theme.Application7Theme
import ru.itis.application7.core.ui.theme.CustomDimensions
import ru.itis.application7.core.ui.theme.CustomStyles

@Composable
fun InputFieldCustom(
    labelText: String? = null,
    placeholderText: String? = null,
    isError: Boolean? = null,
    onSearch: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        var placeholder: @Composable (() -> Unit)? = null
        placeholderText?.let {
            placeholder = {
                Text(
                    text = placeholderText,
                    style = CustomStyles.p3,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        var textState by remember { mutableStateOf(TextFieldValue()) }

        Column {
            labelText?.let {
                Label(labelText)
            }
            TextField(
                value = textState,
                onValueChange = { input ->
                    textState = input
                },
                placeholder = placeholder,
                textStyle = CustomStyles.p3,
                colors = TextFieldDefaults.colors(
                    errorTextColor = MaterialTheme.colorScheme.error,
                    errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = MaterialTheme.colorScheme.error,
                ),
                isError = isError ?: false,
                shape = RoundedCornerShape(CustomDimensions.roundedCorners),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onSearch(textState.text)
                    }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun Label(text: String) {
    Text(
        text = text,
        style = CustomStyles.p3,
        color = MaterialTheme.colorScheme.onTertiary,
        modifier = Modifier
            .padding(bottom = 8.dp, start = 12.dp)
    )
}


@Preview(showBackground = true, apiLevel = 34)
@Composable
fun InputFieldCustomPreview() {
    Application7Theme {
        InputFieldCustom(
            labelText = "Лейбл",
            placeholderText = "Введите текст"
        )
    }
}
