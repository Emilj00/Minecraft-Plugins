package me.emiljoo.minecraftplugins.utilities.data.types

import me.emiljoo.minecraftplugins.utilities.data.PlayerData
import me.emiljoo.minecraftplugins.utilities.data.PlayerDataEntry

open class IntPlayerDataEntry(startingValue: Int, ownerPlayerData: PlayerData) :
    PlayerDataEntry<Int>(startingValue, ownerPlayerData) {}