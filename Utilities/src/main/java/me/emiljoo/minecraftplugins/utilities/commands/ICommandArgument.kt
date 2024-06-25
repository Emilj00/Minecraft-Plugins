package me.emiljoo.minecraftplugins.utilities.commands

import org.bukkit.command.CommandSender

interface ICommandArgument<T> {
    fun parse(sender: CommandSender, input: String): T?
    fun complete(sender: CommandSender, input: String): List<String>
}