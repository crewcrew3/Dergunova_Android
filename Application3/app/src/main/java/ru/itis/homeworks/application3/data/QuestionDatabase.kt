package ru.itis.homeworks.application3.data

import ru.itis.homeworks.application3.models.Answer
import ru.itis.homeworks.application3.models.Question

object QuestionDatabase {

    val questionList: List<Question> = listOf(
        Question (
            id = 0,
            text = "Самая важная дисциплина в ИТИСе?",
            answers = listOf(
                Answer (
                    id = 0,
                    text = "Русский язык",
                ),
                Answer (
                    id = 1,
                    text = "История"
                ),
                Answer (
                    id = 2,
                    text = "Физкультура"
                ),
                Answer (
                    id = 3,
                    text = "Алгебра и геометрия"
                ),
                Answer (
                    id = 4,
                    text = "ОРИС"
                ),
                Answer (
                    id = 5,
                    text = "Право"
                ),
                Answer (
                    id = 6,
                    text = "Основы российской государственности"
                )
            )
        ),

        Question (
            id = 1,
            text = "Любимый принцип SOLID М.М.Абрамского?",
            answers = listOf(
                Answer (
                    id = 0,
                    text = "Принцип Барбары Лисков"
                ),
                Answer (
                    id = 1,
                    text = "Liskov substitution principle"
                ),
                Answer (
                    id = 2,
                    text = "Interface segregation principle"
                ),
                Answer (
                    id = 3,
                    text = "Принцип подстановки Барбары Лисков"
                ),
                Answer (
                    id = 4,
                    text = "Single responsibility principle"
                ),
                Answer (
                    id = 5,
                    text = "LSP"
                )
            )
        ),

        Question (
            id = 2,
            text = "Как зовут преподавательницу, ведущую практику по матстату?",
            answers = listOf(
                Answer (
                    id = 0,
                    text = "София Альфисовна"
                ),
                Answer (
                    id = 1,
                    text = "Софья Альфисовна"
                )
            )
        ),

        Question (
            id = 3,
            text = "Можно ли продлить дедлайн?",
            answers = listOf(
                Answer (
                    id = 0,
                    text = "Нет"
                ),
                Answer (
                    id = 1,
                    text = "Конечно! (ложь)"
                )
            )
        ),

        Question (
            id = 4,
            text = "Кто самый крутой специалист в математической логике?",
            answers = listOf(
                Answer (
                    id = 0,
                    text = "Алан Тьюринг"
                ),
                Answer (
                    id = 1,
                    text = "Джон фон Нейман"
                ),
                Answer (
                    id = 2,
                    text = "Бертран Рассел"
                ),
                Answer (
                    id = 3,
                    text = "Марат Мирзаевич Арсланов"
                ),
                Answer (
                    id = 4,
                    text = "Эдгар Кодд"
                ),
            )
        ),

        Question (
            id = 5,
            text = "За какое время можно успеть подготовиться к экзамену?",
            answers = listOf(
                Answer (
                    id = 0,
                    text = "Два месяца"
                ),
                Answer (
                    id = 1,
                    text = "Месяц"
                ),
                Answer (
                    id = 2,
                    text = "Две недели"
                ),
                Answer (
                    id = 3,
                    text = "Одна недели"
                ),
                Answer (
                    id = 4,
                    text = "Одна ночь"
                ),
                Answer (
                    id = 5,
                    text = "Что такое готовиться к экзамену?"
                )
            )
        ),

        Question (
            id = 6,
            text = "Котики или собачки?",
            answers = listOf(
                Answer (
                    id = 0,
                    text = "Коты"
                ),
                Answer (
                    id = 1,
                    text = "Собаки"
                ),
                Answer (
                    id = 2,
                    text = "Всех люблю!"
                ),
                Answer (
                    id = 3,
                    text = "Попугаи"
                )
            )
        ),

        Question (
            id = 7,
            text = "Какова вероятность уехать на лифте в двойке с первого этажа если вы идете к третьей паре?",
            answers = listOf(
                Answer (
                    id = 0,
                    text = "0%"
                ),
                Answer (
                    id = 1,
                    text = "100%"
                ),
                Answer (
                    id = 2,
                    text = "50/50"
                )
            )
        ),

        Question (
            id = 8,
            text = "Нужна ли математика программисту?",
            answers = listOf(
                Answer (
                    id = 0,
                    text = "Да, в любом случае"
                ),
                Answer (
                    id = 1,
                    text = "Нет"
                ),
                Answer (
                    id = 2,
                    text = "Зависит от направления"
                )
            )
        )
    )
}