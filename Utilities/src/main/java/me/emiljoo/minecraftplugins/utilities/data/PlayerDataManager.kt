package me.emiljoo.minecraftplugins.utilities.data

import org.bukkit.entity.Player
import java.util.HashMap

class PlayerDataManager {
    private val playerDataMap: HashMap<Player, PlayerData> = HashMap()

    fun addPlayerData(player: Player) {
        if (hasPlayerData(player)) {
            return
        }

        playerDataMap[player] = PlayerData()
    }

    fun findPlayerData(player: Player): PlayerData? {
        if (!hasPlayerData(player)) {
            return null
        }

        return playerDataMap[player]
    }

    fun deletePlayerData(player: Player) {
        if (!hasPlayerData(player)) {
            return
        }

        playerDataMap.remove(player)
    }

    fun hasPlayerData(player: Player): Boolean {
        return playerDataMap.containsKey(player)
    }
}