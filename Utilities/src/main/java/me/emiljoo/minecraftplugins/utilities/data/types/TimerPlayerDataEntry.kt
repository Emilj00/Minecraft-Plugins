package me.emiljoo.minecraftplugins.utilities.data.types

import me.emiljoo.minecraftplugins.utilities.data.PlayerData

class TimerPlayerDataEntry(private val startingValue: Int, ownerPlayerData: PlayerData) :
    IntPlayerDataEntry(startingValue, ownerPlayerData) {

    fun tick() {
        val currentValue = getValue()

        if (isTimerFinished()) {
            return
        }

        setValue(currentValue - 1)
    }

    fun isTimerFinished(): Boolean {
        return getValue() <= 0
    }

    fun resetTimer() {
        setValue(startingValue)
    }

    fun setTimerFinished() {
        setValue(0)
    }
}