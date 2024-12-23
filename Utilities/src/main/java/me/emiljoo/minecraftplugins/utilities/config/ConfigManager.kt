package me.emiljoo.minecraftplugins.utilities.config

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.events.EnhancedEvent
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class ConfigManager(private val plugin: EnhancedPlugin, private val fileName: String) {
    private lateinit var configFile: File
    private lateinit var config: FileConfiguration

    val onConfigChangedEvent: EnhancedEvent<ConfigManager> = EnhancedEvent()

    init {
        createConfig()
    }

    private fun createConfig() {
        configFile = File(plugin.dataFolder, fileName)

        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            plugin.saveResource(fileName, false)
        }

        config = YamlConfiguration.loadConfiguration(configFile)
    }

    fun saveConfig() {
        config.save(configFile)
        onConfigChangedEvent.invoke(this)
    }

    fun reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile)
        onConfigChangedEvent.invoke(this)
    }

    fun getConfig(): FileConfiguration {
        return config
    }
}