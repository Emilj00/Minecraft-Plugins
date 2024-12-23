package me.emiljoo.minecraftplugins.basic.helpers

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.config.ConfigField

object ChatMessagesHelper {
    private val configManager = EnhancedPlugin.getInstance().configManager

    // Default Messages
    private const val DEFAULT_PLAYER_JOIN_MESSAGE = "&e{player} joined the game"
    private const val DEFAULT_PLAYER_QUIT_MESSAGE = "&e{player} left the game"
    private const val DEFAULT_MESSAGE_FORMAT = "<{player}> {message}"
    private val DEFAULT_CHAT_EMOTES = emptyMap<String, String>()

    val playerJoinMessageConfig = configField("chat.player-join-message", DEFAULT_PLAYER_JOIN_MESSAGE)
    val playerQuitMessageConfig = configField("chat.player-quit-message", DEFAULT_PLAYER_QUIT_MESSAGE)
    val playerMessageFormatConfig = configField("chat.message-format", DEFAULT_MESSAGE_FORMAT)

    private val chatEmotesConfig = configField("chat.emotes", DEFAULT_CHAT_EMOTES)

    fun applyEmotes(message: String?): String {
        var safeMessage = message ?: return ""

        chatEmotesConfig.get().forEach { (emote, replacement) ->
            safeMessage = message.replace(emote, replacement)
        }

        return safeMessage
    }

    // Helper Function to Create ConfigField
    private fun <T> configField(path: String, default: T): ConfigField<T> {
        return ConfigField(configManager, path, default)
    }
}