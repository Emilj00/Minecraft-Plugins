package me.emiljoo.minecraftplugins.utilities

import org.bukkit.plugin.java.JavaPlugin

abstract class EnhancedPlugin : JavaPlugin() {
    abstract fun getPluginPrefix(): String
    abstract fun getLogLevel(): LogLevel
}