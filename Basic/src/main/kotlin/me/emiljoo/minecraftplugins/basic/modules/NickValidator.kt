package me.emiljoo.minecraftplugins.basic.modules

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.modules.EnhancedModule
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

class NickValidator : EnhancedModule() {
    private var prefix: String = ""

    override fun onEnable(plugin: EnhancedPlugin) {
        prefix = plugin.getPluginPrefix()
    }

    override fun onDisable(plugin: EnhancedPlugin) {
    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        val player: Player = event.player

        if (player.name.contains(" ")) {
            player.kickPlayer(Messenger.colorize("$prefix Your nick cannot contain SPACE character!"))
        }
    }

}