package me.emiljoo.minecraftplugins.utilities.config

class ConfigField<T>(private val configManager: ConfigManager, private val configFieldPath: String, private val defaultValue: T) {
    @Suppress("UNCHECKED_CAST")
    fun get(): T {
        return configManager.getConfig().get(configFieldPath, defaultValue) as T
    }

    fun set(value: T, saveChangesToFile: Boolean = false) {
        configManager.getConfig().set(configFieldPath, value)

        if (saveChangesToFile) {
            configManager.saveConfig()
        }
    }
}