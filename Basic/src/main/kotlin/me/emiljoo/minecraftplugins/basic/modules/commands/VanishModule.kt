package me.emiljoo.minecraftplugins.basic.modules.commands

import me.emiljoo.minecraftplugins.basic.commands.VanishCommand
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
    private var playerDataManager: PlayerDataManager? = null
    private var enhancedPlugin: EnhancedPlugin? = null

    override fun onEnable(plugin: EnhancedPlugin) {
        playerDataManager = plugin.playerDataManager
        enhancedPlugin = plugin
    }

    override fun onDisable(plugin: EnhancedPlugin) {
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        val player: Player = event.player
        val playerData: PlayerData? = playerDataManager!!.findPlayerData(player)

        if (!playerData!!.hasEntry(VanishCommand.VANISH_ENTRY_NAME)) {
            val vanishEntry = BoolPlayerDataEntry(false)
            playerData.addDataEntry(VanishCommand.VANISH_ENTRY_NAME, vanishEntry)
            return
        }

        Bukkit.getOnlinePlayers().forEach { onlinePlayer ->
            run {
                val onlinePlayerData: PlayerData? = playerDataManager!!.findPlayerData(onlinePlayer)
                val vanishPlayerData: BoolPlayerDataEntry =
                    onlinePlayerData!!.getDataEntry(VanishCommand.VANISH_ENTRY_NAME) as BoolPlayerDataEntry

                if (vanishPlayerData.getValue()) {
                    player.hidePlayer(enhancedPlugin!!, onlinePlayer)
                } else {
                    player.showPlayer(enhancedPlugin!!, onlinePlayer)
                }
            }
        }

        val vanishEntry = playerData.getDataEntry(VanishCommand.VANISH_ENTRY_NAME) as BoolPlayerDataEntry

        if (!vanishEntry.getValue()) {
            return
        }

        event.joinMessage = ""
    }

    @EventHandler
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        val player: Player = event.player
        val playerData: PlayerData? = playerDataManager!!.findPlayerData(player)

        val vanishEntry = playerData!!.getDataEntry(VanishCommand.VANISH_ENTRY_NAME) as BoolPlayerDataEntry

        if (!vanishEntry.getValue()) {
            return
        }

        event.quitMessage = ""
    }
}