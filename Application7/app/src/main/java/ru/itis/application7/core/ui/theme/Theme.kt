package ru.itis.application7.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = CustomColors.DarkPrimaryColor,
    secondary = CustomColors.DarkSecondaryColor,
//    background = CustomColors.DarkBackgroundColor,
//    surface = CustomColors.DarkSurfaceColor,
//    onPrimary = CustomColors.DarkTypographyColor,
//    onSecondary = CustomColors.DarkTypographyColor,
//    onBackground = CustomColors.DarkTypographyColor,
//    onSurface = CustomColors.DarkTypographyColor,
)

private val LightColorScheme = lightColorScheme(
    primary = CustomColors.LightPrimaryColor,
    secondary = CustomColors.LightSecondaryColor,
    background = CustomColors.LightBackgroundColor,
    surface = CustomColors.LightSurfaceColor,
    onPrimary = CustomColors.LightOnPrimary,
    onSecondary = CustomColors.LightTypographyColor,
    onBackground = CustomColors. LightTypographyColor,
    onSurface = CustomColors.LightTypographyColor,
    surfaceVariant = CustomColors.LightSurfaceVariant,
    onSurfaceVariant = CustomColors.LightOnSurfaceVariant,
    error = CustomColors.LightErrorColor,
    errorContainer = CustomColors.LightErrorContainerColor,
    onTertiary = CustomColors.LightOnTertiary
)

@Composable
fun Application7Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}