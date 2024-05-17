package me.emiljoo.minecraftplugins.utilities.data

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.modules.EnhancedModule
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerDataRegisterer : EnhancedModule() {
    private var playerDataManager: PlayerDataManager? = null

    override fun onEnable(plugin: EnhancedPlugin) {
        playerDataManager = plugin.playerDataManager
    }

    override fun onDisable(plugin: EnhancedPlugin) {
    }

    @EventHandler
    private fun playerJoinEvent(event: PlayerJoinEvent) {
        val player = event.player
        playerDataManager!!.addPlayerData(player)
    }

    @EventHandler
    private fun playerQuitEvent(event: PlayerQuitEvent) {
        val player = event.player
        playerDataManager!!.deletePlayerData(player)
    }
}