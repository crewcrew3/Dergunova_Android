package ru.itis.application7.authorization.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.application7.authorization.state.AuthorizationScreenEvent
import ru.itis.application7.authorization.state.AuthorizationScreenState
import ru.itis.application7.core.R
import ru.itis.application7.core.ui.BaseScreen
import ru.itis.application7.core.ui.components.InputFieldCustom
import ru.itis.application7.core.ui.components.PrimaryButtonCustom
import ru.itis.application7.core.ui.theme.Application7Theme
import ru.itis.application7.core.ui.theme.CustomDimensions
import ru.itis.application7.core.ui.theme.CustomStyles

@Composable
fun AuthorizationScreen(
    toRegistrationScreen: () -> Unit,
    toListContentScreen: () -> Unit,
    viewModel: AuthorizationViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val pageState by viewModel.pageState.collectAsState(initial = AuthorizationScreenState.Initial)
    when (pageState) {
        is AuthorizationScreenState.Error -> {
            Toast.makeText(
                context,
                (pageState as AuthorizationScreenState.Error).message,
                Toast.LENGTH_SHORT
            ).show()
        }
        is AuthorizationScreenState.OnAuthSuccess -> {
            toListContentScreen()
        }
        else -> Unit
    }

    var nickname by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    BaseScreen { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Spacer(Modifier.height(CustomDimensions.spacerHeightAuthScreens))

            Text(
                text = stringResource(R.string.login_header),
                style = CustomStyles.authHeader
            )

            InputFieldCustom(
                labelText = stringResource(R.string.nickname_text),
                onValueChange = {nickname = it},
                modifier = Modifier
                    .padding(top = CustomDimensions.basePadding)
            )

            InputFieldCustom(
                labelText = stringResource(R.string.password_text),
                onValueChange = {password = it},
                modifier = Modifier
                    .padding(top = CustomDimensions.basePadding)
            )

            PrimaryButtonCustom(
                onBtnText = stringResource(R.string.login_action),
                modifier = Modifier
                    .padding(top = CustomDimensions.basePadding)
            ) {
                viewModel.reduce(
                    event = AuthorizationScreenEvent.OnLogIn(
                        nickname = nickname,
                        password = password
                    )
                )
            }

            Text(
                text = stringResource(R.string.login_to_sign_up_text),
                color = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier
                    .padding(top = CustomDimensions.basePadding)
                    .clickable { toRegistrationScreen() },
            )
        }
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun AuthorizationScreenPreview() {
    Application7Theme {
        AuthorizationScreen(
            {}, {},
        )
    }
}