package me.emiljoo.minecraftplugins.basic.helpers

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.config.ConfigField
import me.emiljoo.minecraftplugins.utilities.data.PlayerData
import me.emiljoo.minecraftplugins.utilities.data.types.InGamePlayerDataEntry
import org.bukkit.entity.Player

object MessageHelper {
    const val MSG_PLAYER_DATA_KEY = "msg-command"

    private val configManager = EnhancedPlugin.getInstance().configManager
    private val playerDataManager = EnhancedPlugin.getInstance().playerDataManager
    private val messenger = EnhancedPlugin.getMessenger()

    val messageToFormat: ConfigField<String> =
        ConfigField(configManager, "msg-and-respond.message-to-format", "&o&7You whisper to {target}: {message}")
    val messageFromFormat: ConfigField<String> =
        ConfigField(configManager, "msg-and-respond.message-from-format", "&o&7{sender} whispers to you: {message}")
    val haventMessagedAnybodyError: ConfigField<String> =
        ConfigField(configManager, "msg-and-respond.havent-messaged-anybody-error", "&4You haven't messaged anybody!")

    fun sendMessageAndHandleData(
        sender: Player,
        target: Player,
        message: String,
    ) {
        val toMessage = messageToFormat
            .get()
            .replace("{target}", target.name)
            .replace("{sender}", sender.name)
            .replace("{message}", message)

        val fromMessage = messageFromFormat
            .get()
            .replace("{target}", target.name)
            .replace("{sender}", sender.name)
            .replace("{message}", message)

        messenger.toCommandSender(sender, toMessage, false)
        messenger.toCommandSender(target, fromMessage, false)

        val senderData: PlayerData? = playerDataManager.findPlayerData(sender)
        handlePlayerData(senderData, target)

        val targetData: PlayerData? = playerDataManager.findPlayerData(target)
        handlePlayerData(targetData, sender)
    }

    private fun handlePlayerData(playerData: PlayerData?, playerToSave: Player) {
        if (playerData == null) {
            return
        }

        val playerDataEntry = InGamePlayerDataEntry(playerToSave, playerData)
        playerData.addDataEntry(MSG_PLAYER_DATA_KEY, playerDataEntry)
    }
}