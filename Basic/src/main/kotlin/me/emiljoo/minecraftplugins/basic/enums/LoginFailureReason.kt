package me.emiljoo.minecraftplugins.basic.enums

import me.emiljoo.minecraftplugins.basic.helpers.AccountHelper

enum class LoginFailureReason {
    USER_NOT_REGISTERED,
    INCORRECT_PASSWORD,
    USER_ALREADY_LOGGED_IN;

    companion object {
        private var userNotRegisteredMessage: String = AccountHelper.userNotRegisteredMessageConfig.get()
        private var incorrectPasswordMessage: String = AccountHelper.incorrectPasswordMessageConfig.get()
        private var userAlreadyLoggedInMessage: String = AccountHelper.userAlreadyLoggedInMessageConfig.get()
    }

    fun getMessage(): String {
        return when (this) {
            USER_NOT_REGISTERED -> userNotRegisteredMessage
            INCORRECT_PASSWORD -> incorrectPasswordMessage
            USER_ALREADY_LOGGED_IN -> userAlreadyLoggedInMessage
        }
    }
}