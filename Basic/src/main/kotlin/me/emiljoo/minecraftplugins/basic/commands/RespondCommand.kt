package me.emiljoo.minecraftplugins.basic.commands

import me.emiljoo.minecraftplugins.basic.helpers.MessageHelper
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.commands.EnhancedCommand
import me.emiljoo.minecraftplugins.utilities.data.PlayerData
import me.emiljoo.minecraftplugins.utilities.data.PlayerDataManager
import me.emiljoo.minecraftplugins.utilities.data.types.PlayerEntityDataEntry
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RespondCommand : EnhancedCommand(
    "respond",
    "basic.player",
    aliases = listOf("r"),
    usage = "/respond <message>"
) {
    private val playerDataManager: PlayerDataManager = EnhancedPlugin.getInstance().playerDataManager

    override fun onCommandExecution(sender: CommandSender, commandLabel: String, args: List<Any?>, messenger: Messenger) {
        if (sender !is Player) {
            messenger.toCommandSender(sender, "You can only use this command as a player!")
            return
        }

        if (args.isEmpty()) {
            messenger.toCommandSender(sender, usage)
            return
        }

        val playerData: PlayerData? = playerDataManager.findPlayerData(sender)
        if (playerData == null) {
            messenger.toCommandSender(sender, MessageHelper.haventMessagedAnybodyErrorConfig.get())
            return
        }

        val lastMessagedPlayerDataEntry: PlayerEntityDataEntry? =
            playerData.getDataEntry(MsgCommand.MSG_PLAYER_DATA_KEY) as? PlayerEntityDataEntry

        if (lastMessagedPlayerDataEntry == null) {
            messenger.toCommandSender(sender, MessageHelper.haventMessagedAnybodyErrorConfig.get())
            return
        }

        val lastMessagedPlayer: Player? = lastMessagedPlayerDataEntry.getValue()
        if (lastMessagedPlayer == null || !lastMessagedPlayer.isOnline) {
            messenger.toCommandSender(sender, MessageHelper.haventMessagedAnybodyErrorConfig.get())
            return
        }

        val message = args.joinToString(" ")

        MessageHelper.sendMessageAndHandleData(
            sender,
            lastMessagedPlayer,
            message,
        )
    }
}
