package me.emiljoo.minecraftplugins.basic.modules

import me.emiljoo.minecraftplugins.basic.controllers.SitController
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.modules.EnhancedModule
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDismountEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent

class SitModule : EnhancedModule() {
    override fun onEnable(plugin: EnhancedPlugin) {
        SitController.initialize(plugin)
    }

    override fun onDisable(plugin: EnhancedPlugin) {}

    @EventHandler
    private fun vehicleExitEvent(event: EntityDismountEvent) {
        val player: Player = event.entity as Player
        SitController.unsitPlayer(player)
    }

    @EventHandler
    private fun onPlayerInteract(event: PlayerInteractEvent) {
        val player: Player = event.player

        if (!event.hasBlock()) {
            return
        }

        if (event.action != Action.RIGHT_CLICK_BLOCK) {
            return
        }

        if (!SitController.sitableBlocks.contains(event.clickedBlock!!.type)) {
            return
        }

        val blockPosition = event.clickedBlock!!.location.subtract(-0.5, 0.0, -0.5)
        SitController.sitPlayer(player, blockPosition)
    }

    @EventHandler
    private fun onPlayerHit(event: EntityDamageEvent) {
        if (event.entity !is Player) {
            return
        }

        val player: Player = event.entity as Player
        SitController.unsitPlayer(player)
    }

    @EventHandler
    private fun onPlayerKill(event: PlayerDeathEvent) {
        val player: Player = event.entity
        SitController.unsitPlayer(player)
    }

    @EventHandler
    private fun onPlayerKick(event: PlayerKickEvent) {
        val player: Player = event.player
        SitController.unsitPlayer(player)
    }

    @EventHandler
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        val player: Player = event.player
        SitController.unsitPlayer(player)
    }
}
