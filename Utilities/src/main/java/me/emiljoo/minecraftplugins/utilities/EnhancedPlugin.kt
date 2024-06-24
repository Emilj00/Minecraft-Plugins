package me.emiljoo.minecraftplugins.utilities

import me.emiljoo.minecraftplugins.utilities.commands.CommandManager
import me.emiljoo.minecraftplugins.utilities.config.ConfigManager
import me.emiljoo.minecraftplugins.utilities.data.PlayerDataManager
import me.emiljoo.minecraftplugins.utilities.modules.ModuleManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin


abstract class EnhancedPlugin : JavaPlugin() {
    companion object {
        private var instance: EnhancedPlugin? = null

        fun getInstance(): EnhancedPlugin {
            return instance ?: throw IllegalStateException("Plugin instance is not initialized")
        }

        fun getMessenger(): Messenger {
            return getInstance().messenger
        }
    }

    val commandManager: CommandManager = CommandManager()
    val messenger: Messenger = Messenger(this)
    val playerDataManager: PlayerDataManager = PlayerDataManager()
    val configManager: ConfigManager = ConfigManager(this, "config.yml")
    private val moduleManager: ModuleManager = ModuleManager()

    override fun onEnable() {
        instance = this

        super.onEnable()

        commandManager.registerCommands(this)
        moduleManager.registerModules(this)

        moduleManager.onPluginStarted(this)
    }

    override fun onDisable() {
        super.onDisable()

        Bukkit.getOnlinePlayers().forEach { player: Player? -> player?.kickPlayer("Stopping server...") }
        moduleManager.onPluginStopped(this)
    }

    abstract fun getPluginPrefix(): String
    abstract fun getLogLevel(): LogLevel

    fun getPluginPackage(): String {
        return this.javaClass.packageName
    }
}
