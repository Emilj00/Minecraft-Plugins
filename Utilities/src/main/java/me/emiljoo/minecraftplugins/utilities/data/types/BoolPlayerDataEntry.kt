package me.emiljoo.minecraftplugins.utilities.data.types

import me.emiljoo.minecraftplugins.utilities.data.PlayerData
import me.emiljoo.minecraftplugins.utilities.data.PlayerDataEntry

class BoolPlayerDataEntry(startValue: Boolean, ownerPlayerData: PlayerData) :
    PlayerDataEntry<Boolean>(startValue, ownerPlayerData) {
        
    fun toggleValue() {
        setValue(!getValue())
    }

    fun setBoolValue(newValue: Boolean) {
        setValue(newValue)
    }
}