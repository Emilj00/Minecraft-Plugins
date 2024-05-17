package me.emiljoo.minecraftplugins.utilities.data.types

class TimerPlayerDataEntry(private val startingValue: Int) : IntPlayerDataEntry(startingValue) {
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