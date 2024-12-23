package me.emiljoo.minecraftplugins.utilities.data

import org.bukkit.entity.Player

class PlayerData(private val ownerPlayer: Player) {
    private val playerDataEntryList: HashMap<String, PlayerDataEntry<*>> = HashMap();

    fun setDataEntry(entryName: String, dataEntry: PlayerDataEntry<*>) {
        playerDataEntryList[entryName] = dataEntry;
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