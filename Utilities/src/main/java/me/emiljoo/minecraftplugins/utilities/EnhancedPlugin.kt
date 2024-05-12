package me.emiljoo.minecraftplugins.utilities

import me.emiljoo.minecraftplugins.utilities.commands.CommandManager
import org.bukkit.plugin.java.JavaPlugin


abstract class EnhancedPlugin : JavaPlugin() {
    companion object {
        private var instance: EnhancedPlugin? = null

        fun getMessenger(): Messenger {
            return instance?.messenger ?: throw IllegalStateException("Messenger is not initialized")
        }
    }

    val messenger = Messenger(this)
    private val commandManager = CommandManager(this)

    override fun onEnable() {
        super.onEnable()
        instance = this

        commandManager.registerCommands()
    }

    abstract fun getPluginPrefix(): String
    abstract fun getLogLevel(): LogLevel

    fun getPluginPackage(): String {
        return this.javaClass.packageName
    }
}
