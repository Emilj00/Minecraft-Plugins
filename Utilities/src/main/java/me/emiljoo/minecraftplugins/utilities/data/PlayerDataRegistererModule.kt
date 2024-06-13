package me.emiljoo.minecraftplugins.utilities.data

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.LogLevel
import me.emiljoo.minecraftplugins.utilities.modules.EnhancedModule
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent

class PlayerDataRegistererModule : EnhancedModule() {
    private lateinit var playerDataManager: PlayerDataManager;

    override fun onEnable(plugin: EnhancedPlugin) {
        playerDataManager = plugin.playerDataManager
    }

    override fun onDisable(plugin: EnhancedPlugin) {
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private fun playerJoinEvent(event: PlayerJoinEvent) {
        playerDataManager.addPlayerData(event.player)
    }
}