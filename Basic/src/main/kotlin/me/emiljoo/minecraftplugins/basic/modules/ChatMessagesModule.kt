package me.emiljoo.minecraftplugins.basic.modules

import me.emiljoo.minecraftplugins.basic.helpers.ChatMessagesHelper
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
        val player: Player = event.player
        val message: String = ChatMessagesHelper.playerJoinMessageConfig.get().replace("{player}", player.name)

        event.joinMessage = Messenger.colorize(message)
    }

    @EventHandler
    private fun onPlayerLeave(event: PlayerQuitEvent) {
        val player: Player = event.player
        val message: String = ChatMessagesHelper.playerQuitMessageConfig.get().replace("{player}", player.name)

        event.quitMessage = Messenger.colorize(message)
    }

    @EventHandler
    private fun onAsyncPlayerChat(event: AsyncPlayerChatEvent) {
        val messageFormat = ChatMessagesHelper.playerMessageFormatConfig
            .get()
            .replace("{player}", "%1\$s")
            .replace("{message}", "%2\$s")

        val message = event.message.split(" ").toMutableList()
        val playersOnWhichSoundWasPlayed: MutableList<Player> = mutableListOf()

        for (i in message.indices) {
            var wordInMessage: String = ChatMessagesHelper.applyEmotes(message[i]);

            val playerName: String = wordInMessage.replace('@', ' ').trim()
            val player: Player? = Bukkit.getPlayerExact(playerName)

            if (player != null && player.isOnline) {
                wordInMessage = "&b&l@${player.name}&r"

                if (!playersOnWhichSoundWasPlayed.contains(player)) {
                    player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 100.0f)
                    playersOnWhichSoundWasPlayed.add(player)
                }
            }

            message[i] = wordInMessage
        }

        event.message = Messenger.colorize(message.joinToString(" "))
        event.format = Messenger.colorize(messageFormat)
    }
}