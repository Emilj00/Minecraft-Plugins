package me.emiljoo.minecraftplugins.basic.controllers

import me.emiljoo.minecraftplugins.basic.commands.SitCommand
import me.emiljoo.minecraftplugins.basic.modules.commands.SitModule
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.isGrounded
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

private data class SittingData(val playerLocationBeforeSit: Location, val chairArmorStand: ArmorStand)

class SitController(private val sitModule: SitModule) {
    companion object {
        private var sitCommand: SitCommand? = null
        private val sittingPlayers: HashMap<Player, SittingData> = HashMap()

        val sitableBlocks: Array<Material> = arrayOf(Material.OAK_STAIRS, Material.DARK_OAK_STAIRS)

        fun initialize(plugin: EnhancedPlugin) {
            sitCommand = plugin.commandManager.getCommand("sit") as SitCommand
            sitCommand!!.enableCommand()
        }

        fun sitPlayer(player: Player) {
            if (!player.isGrounded()) {
                return
            }

            val playerLocation: Location = player.location.clone()
            sitPlayer(player, playerLocation)
        }

        fun sitPlayer(player: Player, location: Location) {
            val playerLocationBeforeSit = player.location.clone()

            val chair: ArmorStand = createChair(player.location, location)
            chair.addPassenger(player)

            sittingPlayers[player] = SittingData(playerLocationBeforeSit, chair)
        }

        fun unsitPlayer(player: Player) {
            if (!isPlayerSitting(player)) {
                return
            }

            val sittingData: SittingData = sittingPlayers[player]!!
            val (location, chair) = sittingData

            chair.removePassenger(player)
            player.teleport(location)

            chair.remove()

            sittingPlayers.remove(player)
        }

        fun togglePlayerSit(player: Player) {
            if (isPlayerSitting(player)) {
                unsitPlayer(player)
            } else {
                sitPlayer(player)
            }
        }

        fun isPlayerSitting(player: Player): Boolean {
            return sittingPlayers[player] != null
        }

        private fun createChair(playerLocation: Location, chairLocation: Location): ArmorStand {
            val chairWorld: World? = chairLocation.world
            val armorStand: ArmorStand = chairWorld!!.spawnEntity(chairLocation.subtract(0.0, 1.8, 0.0), EntityType.ARMOR_STAND) as ArmorStand

            armorStand.isSilent = true
            armorStand.isCollidable = false
            armorStand.isVisible = false
            armorStand.setGravity(false)
            armorStand.setRotation(playerLocation.yaw, playerLocation.pitch)

            return armorStand
        }
    }
}