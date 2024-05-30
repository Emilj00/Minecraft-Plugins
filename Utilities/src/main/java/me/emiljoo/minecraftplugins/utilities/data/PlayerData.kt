package me.emiljoo.minecraftplugins.utilities.data

import org.bukkit.entity.Player
import java.util.HashMap

class PlayerData(private val ownerPlayer: Player) {
    private val playerDataEntryList: HashMap<String, PlayerDataEntry<*>> = HashMap();

    fun addDataEntry(entryName: String, dataEntry: PlayerDataEntry<*>) {
        if (hasEntry(entryName)) {
            return
        }

        playerDataEntryList[entryName] = dataEntry
    }

    fun getDataEntry(entryName: String): PlayerDataEntry<*>? {
        if (!hasEntry(entryName)) {
            return null
        }

        return playerDataEntryList[entryName]
    }

    fun removeDataEntry(entryName: String) {
        if (!hasEntry(entryName)) {
            return
        }

        playerDataEntryList.remove(entryName)
    }

    fun hasEntry(entryName: String): Boolean {
        return playerDataEntryList.containsKey(entryName)
    }

    fun getOwner(): Player {
        return ownerPlayer;
    }
}