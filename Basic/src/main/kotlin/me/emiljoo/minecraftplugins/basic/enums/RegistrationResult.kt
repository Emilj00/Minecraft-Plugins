package me.emiljoo.minecraftplugins.basic.enums

import me.emiljoo.minecraftplugins.basic.helpers.AccountHelper

enum class RegistrationResult {
    REGISTRATION_SUCCESS,
    REGISTRATION_FAILED;

    companion object {
        private var registrationFailedMessage: String = AccountHelper.registrationFailedConfig.get()
        private var registrationSuccessMessage: String = AccountHelper.registrationSuccessConfig.get()
    }

    fun getMessage(): String {
        return when (this) {
            REGISTRATION_SUCCESS -> registrationFailedMessage
            REGISTRATION_FAILED -> registrationSuccessMessage
        }
    }
}