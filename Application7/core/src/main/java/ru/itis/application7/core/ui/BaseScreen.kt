package ru.itis.application7.core.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import ru.itis.application7.core.ui.components.FloatingActionButtonCustom
import ru.itis.application7.core.ui.components.TopBarCustom
import ru.itis.application7.core.ui.theme.CustomDimensions

@Composable
fun BaseScreen(
    isTopBar: Boolean = false,
    isFloatingActionButton: Boolean = false,
    topBarHeader: String = "",
    onFloatingActBtnClick: () -> Unit = {},
    floatingBtnText: String = "",
    topInset: Dp = CustomDimensions.baseInsets,
    bottomInset: Dp = CustomDimensions.baseInsets,
    leftInset: Dp = CustomDimensions.baseInsets,
    rightInset: Dp = CustomDimensions.baseInsets,
    content: @Composable (PaddingValues) -> Unit,
) {
    val systemInsets = WindowInsets.systemBars
    val customInsets = WindowInsets(
        top = topInset,
        bottom = bottomInset,
        left = leftInset,
        right = rightInset
    )
    Scaffold(
        contentWindowInsets = systemInsets.union(customInsets),
        topBar = {
            if (isTopBar)
                TopBarCustom(
                    topAppBarText = topBarHeader,
                )
        },
        floatingActionButton = {
            if (isFloatingActionButton)
                FloatingActionButtonCustom(
                    onClick = onFloatingActBtnClick,
                    text = floatingBtnText
                )
        },
        content = content,
    )
}