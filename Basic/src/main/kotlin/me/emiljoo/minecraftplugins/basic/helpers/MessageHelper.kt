package me.emiljoo.minecraftplugins.basic.helpers

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.config.ConfigField
import me.emiljoo.minecraftplugins.utilities.data.types.PlayerEntityDataEntry
import org.bukkit.entity.Player

object MessageHelper {
    const val MSG_PLAYER_DATA_KEY = "msg-command"

    private val configManager = EnhancedPlugin.getInstance().configManager
    private val playerDataManager = EnhancedPlugin.getInstance().playerDataManager
    private val messenger = EnhancedPlugin.getMessenger()

    private const val DEFAULT_MESSAGE_TO_FORMAT = "&o&7You whisper to {target}: {message}"
    private const val DEFAULT_MESSAGE_FROM_FORMAT = "&o&7{sender} whispers to you: {message}"
    private const val DEFAULT_HAVENT_MESSAGED_ANYBODY_ERROR = "&4You haven't messaged anybody!"

    private val messageToFormatConfig = configField("msg-and-respond.message-to-format", DEFAULT_MESSAGE_TO_FORMAT)
    private val messageFromFormatConfig = configField("msg-and-respond.message-from-format", DEFAULT_MESSAGE_FROM_FORMAT)

    val haventMessagedAnybodyErrorConfig =
        configField("msg-and-respond.havent-messaged-anybody-error", DEFAULT_HAVENT_MESSAGED_ANYBODY_ERROR)

    fun sendMessageAndHandleData(sender: Player, target: Player, message: String) {
        val toMessage = formatMessage(messageToFormatConfig.get(), sender.name, target.name, message)
        val fromMessage = formatMessage(messageFromFormatConfig.get(), sender.name, target.name, message)

        messenger.toCommandSender(sender, toMessage, false)
        messenger.toCommandSender(target, fromMessage, false)

        handlePlayerData(sender, target)
        handlePlayerData(target, sender)
    }

    private fun formatMessage(format: String, sender: String, target: String, message: String): String {
        return format
            .replace("{sender}", sender)
            .replace("{target}", target)
            .replace("{message}", message)
    }

    private fun handlePlayerData(player: Player, playerToSave: Player) {
        val playerData = playerDataManager.findPlayerData(player) ?: return
        val playerDataEntry = PlayerEntityDataEntry(playerToSave, playerData)
        playerData.setDataEntry(MSG_PLAYER_DATA_KEY, playerDataEntry)
    }

    private fun configField(path: String, default: String): ConfigField<String> {
        return ConfigField(configManager, path, default)
    }
}