package me.emiljoo.minecraftplugins.basic.commands

import me.emiljoo.minecraftplugins.basic.helpers.MessageHelper
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.commands.EnhancedCommand
import me.emiljoo.minecraftplugins.utilities.commands.arguments.PlayerArgument
import me.emiljoo.minecraftplugins.utilities.data.PlayerDataManager
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MsgCommand : EnhancedCommand(
    "msg",
    "basic.player",
    aliases = listOf("w"),
    usage = "/msg <player> <message>"
) {
    companion object {
        const val MSG_PLAYER_DATA_KEY = "msg-command"
    }

    private val playerDataManager: PlayerDataManager = EnhancedPlugin.getInstance().playerDataManager

    init {
        addArgument(PlayerArgument())
    }

    override fun onCommandExecution(sender: CommandSender, commandLabel: String, args: List<Any?>, messenger: Messenger) {
        if (sender !is Player) {
            messenger.toCommandSender(sender, "You can only use this command as a player!")
            return
        }

        if (args.size < 2) {
            messenger.toCommandSender(sender, usage)
            return
        }

        val target: Player? = args[0] as? Player
        if (target == null) {
            messenger.toCommandSender(sender, "Player is not online.")
            return
        }

        val message = args.drop(1).joinToString(" ")

        MessageHelper.sendMessageAndHandleData(
            sender,
            target,
            message
        )
    }
}
