package me.emiljoo.minecraftplugins.basic.modules

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.modules.EnhancedModule
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random


class InventorySaver : EnhancedModule() {
    override fun onEnable(plugin: EnhancedPlugin) {

    }

    override fun onDisable(plugin: EnhancedPlugin) {
    }

    @EventHandler
    private fun onPlayerDeath(event: PlayerDeathEvent) {
        // It's confusing asf.
        // it keeps your inventory on death but also
        // drops everything what is inside your inventory
        event.keepInventory = true

        val player = event.entity

        val world: World = player.location.world!!
        if (world.isGameRule("keepInventory")) {
            event.drops.clear()

            return
        }

        val drops: MutableList<ItemStack> = event.drops
        val inventory = player.inventory.contents

        for (i in inventory.indices) {
            if (inventory[i] != null) {
                if (Random.nextBoolean()) { //TODO: Set it for percentage and Config it
                    drops.remove(inventory[i])
                } else {
                    inventory[i] = null
                }
            }
        }

        player.inventory.contents = inventory
    }
}