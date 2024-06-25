package me.emiljoo.minecraftplugins.basic.modules

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.config.ConfigField
import me.emiljoo.minecraftplugins.utilities.config.ConfigManager
import me.emiljoo.minecraftplugins.utilities.modules.EnhancedModule
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random


class InventorySaver : EnhancedModule() {
    private lateinit var percentageOfKeepInventory: ConfigField<Int>

    override fun onEnable(plugin: EnhancedPlugin) {
        val configManager: ConfigManager = plugin.configManager

        percentageOfKeepInventory = ConfigField(configManager, "inventory-saver.percentage-of-keep-inventory", 50)
    }

    override fun onDisable(plugin: EnhancedPlugin) {
    }

    @EventHandler
    private fun onPlayerDeath(event: PlayerDeathEvent) {
        val percentageOfKeepInventoryValue = percentageOfKeepInventory.get()

        if (percentageOfKeepInventoryValue <= 0) {
            return
        }

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
                val randomNumber = Random.nextInt(0, 2)
                if (randomNumber > percentageOfKeepInventoryValue) {
                    drops.remove(inventory[i])
                } else {
                    inventory[i] = null
                }
            }
        }

        player.inventory.contents = inventory
    }
}