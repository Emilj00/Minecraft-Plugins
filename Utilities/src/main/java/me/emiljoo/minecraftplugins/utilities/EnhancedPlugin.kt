package me.emiljoo.minecraftplugins.utilities

import me.emiljoo.minecraftplugins.utilities.commands.CommandManager
import me.emiljoo.minecraftplugins.utilities.modules.ModuleManager
import org.bukkit.plugin.java.JavaPlugin


abstract class EnhancedPlugin : JavaPlugin() {
    companion object {
        private var instance: EnhancedPlugin? = null

        fun getMessenger(): Messenger {
            return instance?.messenger ?: throw IllegalStateException("Messenger is not initialized")
        }
    }

    val messenger: Messenger = Messenger(this)
    val commandManager: CommandManager = CommandManager(this)
    private val moduleManager: ModuleManager = ModuleManager(this)

    override fun onEnable() {
        super.onEnable()

        instance = this

        commandManager.registerCommands()
        moduleManager.registerModules()
    }

    override fun onDisable() {
        super.onDisable()

//        Bukkit.getOnlinePlayers().forEach { player: Player? -> player?.kickPlayer("Restarting server...") }

        moduleManager.onPluginStopped()
    }

    abstract fun getPluginPrefix(): String
    abstract fun getLogLevel(): LogLevel

    fun getPluginPackage(): String {
        return this.javaClass.packageName
    }
}
