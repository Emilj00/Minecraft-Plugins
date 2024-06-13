package me.emiljoo.minecraftplugins.utilities.data

import org.bukkit.entity.Player
import java.util.HashMap
import java.util.UUID

class PlayerDataManager {
    private val playerDataMap: MutableMap<UUID, PlayerData> = mutableMapOf()

    fun addPlayerData(player: Player) {
        playerDataMap[player.uniqueId] = PlayerData(player)
    }

    fun findPlayerData(player: Player): PlayerData? {
        if (!hasPlayerData(player)) {
            return null
        }

        return playerDataMap[player.uniqueId]
    }

    fun deletePlayerData(player: Player) {
        if (!hasPlayerData(player)) {
            return
        }

        playerDataMap.remove(player.uniqueId)
    }

    private fun hasPlayerData(player: Player): Boolean {
        return playerDataMap.containsKey(player.uniqueId)
    }
}