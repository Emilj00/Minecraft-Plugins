package me.emiljoo.minecraftplugins.utilities.data

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.modules.EnhancedModule
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent

class PlayerDataRegistererModule : EnhancedModule() {
    private var playerDataManager: PlayerDataManager? = null

    override fun onEnable(plugin: EnhancedPlugin) {
        playerDataManager = plugin.playerDataManager
    }

    override fun onDisable(plugin: EnhancedPlugin) {
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private fun playerJoinEvent(event: PlayerJoinEvent) {
        val player = event.player
        playerDataManager!!.addPlayerData(player)
    }
}