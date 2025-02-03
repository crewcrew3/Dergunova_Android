package ru.itis.application5.compose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineDispatcher
import ru.itis.application5.utils.CoroutinesParameters
import ru.itis.application5.R

@Composable
fun MainContent() {
    Column {
        //поле для ввода текста
        TextField(
            value = CoroutinesParameters.numberOfCoroutines?.toString() ?: "",
            onValueChange = { input ->
                CoroutinesParameters.numberOfCoroutines = input.toIntOrNull()
            },
            label = { Text(text = stringResource(R.string.text_field_label)) },
            modifier = Modifier
                .fillMaxWidth(),
            //если число корутинов налл - то переводим поле в состояние ошибки
            isError = CoroutinesParameters.numberOfCoroutines == null
        )

        //две радио-группы
        Row(
            modifier = Modifier
                .padding(top = 32.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            //первая группа
            Column(
                modifier = Modifier
                    .padding(end = 32.dp)
            ) {
                Row {
                    RadioButton(
                        selected = CoroutinesParameters.isParallel == false,
                        onClick = { CoroutinesParameters.isParallel = false },
                    )
                    Text(
                        text = stringResource(R.string.first_param_one_by_one),
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(top = 16.dp)
                    )
                }

                Row {
                    RadioButton(
                        selected = CoroutinesParameters.isParallel == true,
                        onClick = { CoroutinesParameters.isParallel = true },
                    )
                    Text(
                        text = stringResource(R.string.first_param_in_parallel),
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(top = 16.dp)
                    )
                }
            }

            //вторая
            Column {
                Row {
                    RadioButton(
                        selected = CoroutinesParameters.isContinue == false,
                        onClick = { CoroutinesParameters.isContinue = false },
                    )
                    Text(
                        text = stringResource(R.string.second_param_not_continue_in_the_background),
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(top = 8.dp)
                    )
                }

                Row {
                    RadioButton(
                        selected = CoroutinesParameters.isContinue == true,
                        onClick = { CoroutinesParameters.isContinue = true },
                    )
                    Text(
                        text = stringResource(R.string.second_param_continue_in_the_background),
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun DropDownMenu(
    items: List<CoroutineDispatcher>,
) {

    var isMenuOpen by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        //выпадающий список
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = selectedItem.ifEmpty {
                    stringResource(R.string.drop_down_menu_label)
                },
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(top = 16.dp)
            )
            Box {
                IconButton(onClick = { isMenuOpen = !isMenuOpen }) {
                    Icon(
                        painterResource(R.drawable.ic_menu),
                        contentDescription = stringResource(R.string.icon_menu_desc_more_options)
                    )
                }

                DropdownMenu(
                    expanded = isMenuOpen,
                    onDismissRequest = { isMenuOpen = false }
                ) {
                    items.forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                CoroutinesParameters.pullThread = option
                                selectedItem = option.toString()
                                isMenuOpen = false
                            }
                        ) {
                            Text(option.toString())
                        }
                    }
                }
            }
        }
    }
}


//две кнопки
@Composable
fun ButtonContent(onClickRun: () -> Unit, onClickCancel: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(top = 32.dp)
                .align(Alignment.CenterHorizontally),
        ) {
            Button(
                onClick = {
                    onClickRun()
                },
                modifier = Modifier
                    .padding(end = 64.dp)
            ) {
                Text(
                    text = stringResource(R.string.btn_text_run_coroutines),
                    fontSize = 18.sp,
                )
            }

            Button(
                onClick = {
                    onClickCancel()
                }
            ) {
                Text(
                    text = stringResource(R.string.btn_text_cancel_coroutines),
                    fontSize = 18.sp
                )
            }
        }
    }
}



