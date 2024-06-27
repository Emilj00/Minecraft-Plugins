package me.emiljoo.minecraftplugins.utilities.data.types

import me.emiljoo.minecraftplugins.utilities.data.PlayerData
import me.emiljoo.minecraftplugins.utilities.data.PlayerDataEntry
import org.bukkit.entity.Player

class InGamePlayerDataEntry(value: Player, ownerPlayerData: PlayerData) : PlayerDataEntry<Player>(value, ownerPlayerData) {
    fun setPlayer(player: Player) {
        setValue(player)
    }
}