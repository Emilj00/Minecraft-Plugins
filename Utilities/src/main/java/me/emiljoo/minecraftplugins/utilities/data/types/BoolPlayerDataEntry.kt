package me.emiljoo.minecraftplugins.utilities.data.types

import me.emiljoo.minecraftplugins.utilities.data.PlayerDataEntry

class BoolPlayerDataEntry(startValue: Boolean) : PlayerDataEntry<Boolean>(startValue) {
    fun toggleValue() {
        setValue(!getValue())
    }

    fun setBoolValue(newValue: Boolean) {
        setValue(newValue)
    }
}