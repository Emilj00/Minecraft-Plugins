package me.emiljoo.minecraftplugins.utilities.commands.arguments

import me.emiljoo.minecraftplugins.utilities.commands.ICommandArgument
import org.bukkit.command.CommandSender

class StringArgument : ICommandArgument<String> {
    override fun parse(sender: CommandSender, input: String): String {
        return input
    }

    override fun complete(sender: CommandSender, input: String): List<String> {
        return emptyList()
    }
}