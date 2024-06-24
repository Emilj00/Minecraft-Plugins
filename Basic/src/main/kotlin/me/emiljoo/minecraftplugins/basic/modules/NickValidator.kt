package me.emiljoo.minecraftplugins.basic.modules

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.modules.EnhancedModule
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

class NickValidator : EnhancedModule() {
    private lateinit var pluginPrefix: String
    private val minecraftNickRegex: Regex = "^[a-zA-Z0-9_]{3,16}$".toRegex()

    override fun onEnable(plugin: EnhancedPlugin) {
        pluginPrefix = plugin.getPluginPrefix()
    }

    override fun onDisable(plugin: EnhancedPlugin) {
    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        val player: Player = event.player

        if (!player.name.matches(minecraftNickRegex)) {
            player.kickPlayer(Messenger.colorize("$pluginPrefix Your nick is not valid!"))
        }
    }

}