package ru.itis.application7.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.itis.application7.core.ui.theme.CustomDimensions
import ru.itis.application7.core.ui.theme.CustomStyles

@Composable
fun PrimaryButtonCustom(
    onBtnText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        shape = RoundedCornerShape(CustomDimensions.roundedCorners),
        modifier = modifier
            .fillMaxWidth()
            .height(CustomDimensions.btnHeight)
    ) {
        Text(
            text = onBtnText,
            style = CustomStyles.p2,
        )
    }
}