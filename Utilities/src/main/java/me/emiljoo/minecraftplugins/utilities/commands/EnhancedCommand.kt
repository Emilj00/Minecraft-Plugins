package me.emiljoo.minecraftplugins.utilities.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

abstract class EnhancedCommand(val commandName: String) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, commandLabel: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission(command.permission.toString())) {
            return true
        }

        onCommandExecution(sender, command, commandLabel, args)

        return true
    }

    abstract fun onCommandExecution(sender: CommandSender, command: Command, commandLabel: String, args: Array<out String>)
}
