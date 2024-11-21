package ru.itis.homeworks.application2.recycler_view

sealed class MultipleHolderData(
    open val id: Int,
    open val name: String,
)

class SongHolderData(
    override val id: Int,
    override val name: String,
    val lyrics: String,
    val url: String
) : MultipleHolderData(id, name)

class ButtonHolderData(id: Int, name: String) : MultipleHolderData(id, name)