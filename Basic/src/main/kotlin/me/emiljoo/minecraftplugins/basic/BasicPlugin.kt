package me.emiljoo.minecraftplugins.basic

import me.emiljoo.minecraftplugins.basic.controllers.AccountController
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.LogLevel

class BasicPlugin : EnhancedPlugin() {
    val accountController: AccountController = AccountController(this)

    override fun getPluginPrefix(): String = "&7&l[&4&lB&f&lC&7&l]&r"
    override fun getLogLevel(): LogLevel = LogLevel.Info
}
