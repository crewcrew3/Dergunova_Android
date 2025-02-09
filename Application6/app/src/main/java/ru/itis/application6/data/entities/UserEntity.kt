package ru.itis.application6.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(
        value = ["user_nickname"],
        unique = true
    )]
)
data class UserEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    var userId: Int = 0,

    @ColumnInfo(name = "user_nickname")
    var userNickname: String,

    @ColumnInfo(name = "user_password")
    var userPassword: String,

    @ColumnInfo(name = "user_password_salt")
    var userPasswordSalt: String,

    @ColumnInfo(name = "user_age")
    var userAge: Int?,

    @ColumnInfo(name = "user_about_yourself")
    var userAboutYourself: String?,
)
