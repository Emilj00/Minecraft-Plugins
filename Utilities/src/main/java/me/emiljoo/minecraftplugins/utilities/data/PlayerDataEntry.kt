package me.emiljoo.minecraftplugins.utilities.data

abstract class PlayerDataEntry<T>(private var value: T, private val ownerPlayerData: PlayerData) {
    fun getValue(): T {
        return value
    }

    fun getOwnerPlayerData(): PlayerData {
        return ownerPlayerData
    }

    protected fun setValue(value: T) {
        this.value = value
    }
}