package me.emiljoo.minecraftplugins.basic.modules

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.config.ConfigField
import me.emiljoo.minecraftplugins.utilities.config.ConfigManager
import me.emiljoo.minecraftplugins.utilities.modules.EnhancedModule
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent


class ChatMessagesModule : EnhancedModule() {
    companion object {
        lateinit var playerJoinConfigField: ConfigField<String>
        lateinit var playerQuitConfigField: ConfigField<String>
    }

    private lateinit var playerMessageFormatConfigField: ConfigField<String>

    override fun onEnable(plugin: EnhancedPlugin) {
        val configManager: ConfigManager = plugin.configManager

        playerJoinConfigField = ConfigField(configManager, "chat.player-join-message", "&e{player} joined the game")
        playerQuitConfigField = ConfigField(configManager, "chat.player-quit-message", "&e{player} left the game")
        playerMessageFormatConfigField = ConfigField(configManager, "chat.message-format", "<{player}> {message}")
    }

    override fun onDisable(plugin: EnhancedPlugin) {

    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        val player: Player = event.player
        val message: String = playerJoinConfigField.get().replace("{player}", player.name)

        event.joinMessage = Messenger.colorize(message)
    }

    @EventHandler
    private fun onPlayerLeave(event: PlayerQuitEvent) {
        val player: Player = event.player
        val message: String = playerQuitConfigField.get().replace("{player}", player.name)

        event.quitMessage = Messenger.colorize(message)
    }

    @EventHandler
    private fun onAsyncPlayerChat(event: AsyncPlayerChatEvent) {
        val messageFormat = playerMessageFormatConfigField.get()
            .replace("{player}", "%1\$s")
            .replace("{message}", "%2\$s")

        val message = event.message.split(" ").toMutableList()
        val playersOnWhichSoundWasPlayed: MutableList<Player> = mutableListOf()

        for (i in message.indices) {
            var wordInMessage = message[i]
                .replace(":smile:".toRegex(), "(◕‿◕)")
                .replace(":lenny:".toRegex(), "(͡° ͜ʖ ͡°)")
                .replace(":uwu:".toRegex(), "(˘ω˘)")
                .replace(":confused:".toRegex(), "(•ิ_•ิ)")


            val playerName = wordInMessage.replace('@', ' ').trim()
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