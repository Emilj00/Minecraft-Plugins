package me.emiljoo.minecraftplugins.basic.modules

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.modules.EnhancedModule
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent


class ChatMessagesModule : EnhancedModule() {
    override fun onEnable(plugin: EnhancedPlugin) {

    }

    override fun onDisable(plugin: EnhancedPlugin) {

    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        val message = "&7&l[&2&l+&7&l] &7${event.player.name}"
        event.joinMessage = Messenger.colorize(message)
    }

    @EventHandler
    private fun onPlayerLeave(event: PlayerQuitEvent) {
        val message = "&7&l[&4&l-&7&l] &7${event.player.name}"
        event.quitMessage = Messenger.colorize(message)
    }

    @EventHandler
    private fun onAsyncPlayerChat(event: AsyncPlayerChatEvent) {
        event.format = Messenger.colorize("&f&l%1\$s&r&f: %2\$s")
        val message = event.message.split(" ").toMutableList()

        for (i in message.indices) {
            var wordInMessage = message[i]

            wordInMessage = wordInMessage
                .replace(":smile:".toRegex(), "(◕‿◕)")
                .replace(":lenny:".toRegex(), "(͡° ͜ʖ ͡°)")
                .replace(":uwu:".toRegex(), "(˘ω˘)")
                .replace(":confused:".toRegex(), "(•ิ_•ิ)")

            val playerName = wordInMessage.replace('@', ' ').trim()
            val player: Player? = Bukkit.getPlayer(playerName)

            if (player != null && player.isOnline) {
                wordInMessage = "&b&l@${player.name}&r"

                player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 100.0f)
            }

            message[i] = wordInMessage
        }

        event.message = Messenger.colorize(message.joinToString(" "))
    }

}