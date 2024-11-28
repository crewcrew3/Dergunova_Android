package ru.itis.homeworks.application3.data

import ru.itis.homeworks.application3.models.Answer
import ru.itis.homeworks.application3.models.Question

object QuestionDatabase {

    val questionList: List<Question> = listOf(
        Question (
            id = 0,
            text = "Сколько будет 1+1?",
            answers = listOf(
                Answer (
                    id = 1,
                    text = "1",
                ),
                Answer (
                    id = 2,
                    text = "2"
                ),
                Answer (
                    id = 3,
                    text = "3"
                ),
                Answer (
                    id = 4,
                    text = "Я не знаю"
                ),
                Answer (
                    id = 5,
                    text = "Да"
                ),
            )
        ),

        Question (
            id = 1,
            text = "Любимый принцип из SOLID Абрамского?",
            answers = listOf(
                Answer (
                    id = 1,
                    text = "Open-closed principle"
                ),
                Answer (
                    id = 2,
                    text = "Liskov substitution principle"
                ),
                Answer (
                    id = 3,
                    text = "Interface segregation principle"
                ),
                Answer (
                    id = 4,
                    text = "Single responsibility principle"
                ),
                Answer (
                    id = 5,
                    text = "Dependency inversion principle"
                )
            )
        ),

        Question (
            id = 2,
            text = "Как зовут преподавательницу, которая ведет практику по матстату?",
            answers = listOf(
                Answer (
                    id = 1,
                    text = "София Альфисовна"
                ),
                Answer (
                    id = 2,
                    text = "Софья Альфисовна"
                )
            )
        ),

        Question (
            id = 3,
            text = "Можно ли продлить дедлайн?",
            answers = listOf(
                Answer (
                    id = 1,
                    text = "Нет"
                ),
                Answer (
                    id = 2,
                    text = "Конечно! (ложь)"
                ),
                Answer (
                    id = 3,
                    text = "Земля вам пухом"
                )
            )
        ),

        Question (
            id = 4,
            text = "Я не знаю какой вопрос задать",
            answers = listOf(
                Answer (
                    id = 1,
                    text = "Да"
                ),
                Answer (
                    id = 2,
                    text = "Нет"
                )
            )
        ),

        Question (
            id = 5,
            text = "Кошки или собаки?",
            answers = listOf(
                Answer (
                    id = 1,
                    text = "Кошки"
                ),
                Answer (
                    id = 2,
                    text = "Собаки"
                ),
                Answer (
                    id = 3,
                    text = "Всех люблю"
                )
            )
        )
    )
}