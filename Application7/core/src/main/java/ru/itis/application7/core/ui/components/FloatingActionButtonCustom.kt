package ru.itis.application7.core.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.itis.application7.core.ui.theme.Application7Theme
import ru.itis.application7.core.ui.theme.CustomDimensions
import ru.itis.application7.core.ui.theme.CustomStyles

@Composable
fun FloatingActionButtonCustom(
    onClick: () -> Unit = {},
    text: String = ""
) {
    FloatingActionButton(
        onClick = { onClick() },
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = CustomDimensions.baseElevation,
        ),
        modifier = Modifier
            .width(96.dp)
            .height(56.dp)
    ) {
        Text(
            text = text,
            style = CustomStyles.p2
        )
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun FloatingActionButtonCustomPreview() {
    Application7Theme {
        FloatingActionButtonCustom()
    }
}