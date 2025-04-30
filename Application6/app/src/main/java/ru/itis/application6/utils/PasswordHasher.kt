package ru.itis.application6.utils

import java.security.MessageDigest
import java.security.SecureRandom
import android.util.Base64

object PasswordHasher {

    fun hashPassWithSalt(password: String): Array<String> {
        val random = SecureRandom() // объект для генерации соли
        val salt = ByteArray(16) // соль длиной 16 байт
        random.nextBytes(salt) // заполняем массив соли случайными байтами


        val md = MessageDigest.getInstance("SHA-512") // создаем MessageDigest, который будет использовать алгоритм хэширования SHA-512
        md.update(salt) // добавили данные к экземпляру MessageDigest в виде соли (хэш еще не вычисляется)

        val hashedPassword = md.digest(password.toByteArray()) // хэшируем пароль с ранее добавленной солью

        val hashPassBase64 = Base64.encodeToString(hashedPassword, Base64.NO_WRAP) // кодируем хешированный пароль в строку с использованием Base64
        val saltBase64 = Base64.encodeToString(salt, Base64.NO_WRAP) // закодировали соль в Base64

        return arrayOf(hashPassBase64, saltBase64)

    }

    /* данный метод принимает введенный пароль извне, хэш пароля с которым сравнивается введенный пароль, массив байтов соли
       и делает с введенным паролем все то же самое, что и в методе хэширования. Потом сравниваем результат с хэшом "оригинального" пароля */
    fun verifyPassword(inputPassword: String, storedHash: String, saltBase64: String): Boolean {
            val salt = Base64.decode(saltBase64, Base64.DEFAULT)
            val md = MessageDigest.getInstance("SHA-512")
            md.update(salt)

            val hashedInputPassword = md.digest(inputPassword.toByteArray())
            val newHash = Base64.encodeToString(hashedInputPassword, Base64.NO_WRAP)

            return newHash == storedHash
    }
}
