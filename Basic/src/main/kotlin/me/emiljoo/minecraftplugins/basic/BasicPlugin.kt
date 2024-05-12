package me.emiljoo.minecraftplugins.basic

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.LogLevel

class BasicPlugin : EnhancedPlugin() {
    override fun getPluginPrefix(): String = "[BC]"
    override fun getLogLevel(): LogLevel = LogLevel.Info
}