package me.emiljoo.minecraftplugins.utilities.data

abstract class PlayerDataEntry<T>(private var value: T) {
    fun getValue(): T {
        return value
    }

    protected fun setValue(value: T) {
        this.value = value
    }
}