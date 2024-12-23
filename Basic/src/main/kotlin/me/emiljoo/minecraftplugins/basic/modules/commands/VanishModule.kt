package me.emiljoo.minecraftplugins.basic.modules.commands

import me.emiljoo.minecraftplugins.basic.commands.admin.VanishCommand
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.data.PlayerData
import me.emiljoo.minecraftplugins.utilities.data.PlayerDataManager
import me.emiljoo.minecraftplugins.utilities.data.types.BoolPlayerDataEntry
import me.emiljoo.minecraftplugins.utilities.modules.EnhancedModule
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class VanishModule : EnhancedModule() {
    private lateinit var playerDataManager: PlayerDataManager
    private lateinit var enhancedPlugin: EnhancedPlugin

    override fun onEnable(plugin: EnhancedPlugin) {
        playerDataManager = plugin.playerDataManager
        enhancedPlugin = plugin
    }

    override fun onDisable(plugin: EnhancedPlugin) {
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        val playerThatJoin: Player = event.player
        val playerData: PlayerData = playerDataManager.findPlayerData(playerThatJoin) ?: return

        if (!playerData.hasEntry(VanishCommand.VANISH_ENTRY_NAME)) {
            val vanishEntry = BoolPlayerDataEntry(false, playerData)
            playerData.setDataEntry(VanishCommand.VANISH_ENTRY_NAME, vanishEntry)
        }

        Bukkit.getOnlinePlayers().forEach { onlinePlayer ->
            run {
                val onlinePlayerData: PlayerData = playerDataManager.findPlayerData(onlinePlayer) ?: return
                val vanishPlayerData = onlinePlayerData.getDataEntry(VanishCommand.VANISH_ENTRY_NAME) as BoolPlayerDataEntry

                if (vanishPlayerData.getValue() == true) {
                    playerThatJoin.hidePlayer(enhancedPlugin, onlinePlayer)
                } else {
                    playerThatJoin.showPlayer(enhancedPlugin, onlinePlayer)
                }
            }
        }

        val vanishEntry = playerData.getDataEntry(VanishCommand.VANISH_ENTRY_NAME) as BoolPlayerDataEntry

        if (vanishEntry.getValue() == false) {
            return
        }

        event.joinMessage = ""
    }

    @EventHandler
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        val player: Player = event.player
        val playerData: PlayerData? = playerDataManager.findPlayerData(player)

        val vanishEntry = playerData?.getDataEntry(VanishCommand.VANISH_ENTRY_NAME) as BoolPlayerDataEntry

        if (vanishEntry.getValue() == false) {
            return
        }

        event.quitMessage = ""
    }
}