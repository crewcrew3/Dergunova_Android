package ru.itis.application6.utils

class ValidationHelper {

    companion object {

        fun validateSongData(name: String, year: Int?) : Boolean {
            val flagName = name.isNotBlank()
            var flagYear = true

            year?.let { safeYear ->
                flagYear = (safeYear <= 2025) && (safeYear >= 1600)
            }
            return flagName && flagYear
        }

        fun validateUserData(nickname: String, password: String, age: String?) : Boolean {
            var flagAge = true
            age?.let { safeAge ->
                val regex = "^[1-9]\\d*$".toRegex()
                if (!regex.matches(safeAge)) {
                    flagAge =  false
                } else {
                    if (safeAge.toInt() > 100) {
                        flagAge = false
                    }
                }
            }
            return nickname.isNotBlank() && password.isNotBlank() && flagAge
        }
    }
}