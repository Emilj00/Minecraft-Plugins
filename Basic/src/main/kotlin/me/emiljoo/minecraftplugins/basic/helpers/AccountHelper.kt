package me.emiljoo.minecraftplugins.basic.helpers

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.config.ConfigField
import me.emiljoo.minecraftplugins.utilities.config.ConfigManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World

object AccountHelper {
    private val usersConfigManager = ConfigManager(EnhancedPlugin.getInstance(), "usersdb.yml")

    private const val DEFAULT_SUCCESS_LOGIN_MESSAGE = "You have successfully logged in!"
    private const val DEFAULT_USER_NOT_REGISTERED_MESSAGE = "You are not registered!"
    private const val DEFAULT_INCORRECT_PASSWORD_MESSAGE = "Incorrect password!"
    private const val DEFAULT_USER_ALREADY_LOGGED_IN_MESSAGE = "You are already logged in!"
    private const val DEFAULT_PASSWORD_REGEX = ".{3,}"
    private const val DEFAULT_PASSWORD_FORMAT_INFO = "Password must be at least 3 characters"
    private const val DEFAULT_REGISTRATION_SUCCESS_MESSAGE = "You have successfully registered."
    private const val DEFAULT_REGISTRATION_FAILED_MESSAGE = "Registration failed. You are already registered."
    private const val DEFAULT_LOGOUT_MESSAGE = "You have been successfully logged out."

    val successfulLoginMessageConfig = configField("accounts.login.successful-login-message", DEFAULT_SUCCESS_LOGIN_MESSAGE)
    val userNotRegisteredMessageConfig = configField("accounts.login.user-not-registered-message", DEFAULT_USER_NOT_REGISTERED_MESSAGE)
    val incorrectPasswordMessageConfig = configField("accounts.login.incorrect-password-message", DEFAULT_INCORRECT_PASSWORD_MESSAGE)
    val userAlreadyLoggedInMessageConfig =
        configField("accounts.login.user-already-logged-in-message", DEFAULT_USER_ALREADY_LOGGED_IN_MESSAGE)


    val passwordRegexConfig = configField("accounts.password-regex", DEFAULT_PASSWORD_REGEX)
    val passwordFormatInfoConfig = configField("accounts.password-format-info", DEFAULT_PASSWORD_FORMAT_INFO)

    val registrationSuccessConfig = configField("accounts.register.success-message", DEFAULT_REGISTRATION_SUCCESS_MESSAGE)
    val registrationFailedConfig = configField("accounts.register.failed-message", DEFAULT_REGISTRATION_FAILED_MESSAGE)

    val logoutMessageConfig = configField("accounts.logout.logout-message", DEFAULT_LOGOUT_MESSAGE)

    fun getVoidLocation(): Location {
        val startingWorld: World = Bukkit.getWorlds().firstOrNull() ?: throw IllegalStateException("No worlds found!")
        return Location(startingWorld, 0.0, -80.0, 0.0)
    }

    private fun configField(path: String, default: String): ConfigField<String> {
        return ConfigField(usersConfigManager, path, default)
    }
}