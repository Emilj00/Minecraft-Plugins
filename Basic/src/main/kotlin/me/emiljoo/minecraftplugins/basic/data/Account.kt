package me.emiljoo.minecraftplugins.basic.data

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import java.util.*

data class Account(
    val username: String,
    val password: String,

    var worldUuid: String,
    var xPosition: Double,
    var yPosition: Double,
    var zPosition: Double,
) {
    fun getLocation(): Location {
        val world: World = Bukkit.getWorld(UUID.fromString(worldUuid)) ?: Bukkit.getWorlds()[0]

        return Location(
            world,
            xPosition,
            yPosition,
            zPosition,
        )
    }
}