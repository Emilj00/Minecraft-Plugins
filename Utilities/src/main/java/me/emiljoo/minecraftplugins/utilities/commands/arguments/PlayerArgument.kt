package me.emiljoo.minecraftplugins.utilities.commands.arguments

import me.emiljoo.minecraftplugins.utilities.commands.CommandArgument
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PlayerArgument : CommandArgument<Player> {
    override fun parse(sender: CommandSender, input: String): Player? {
        return Bukkit.getPlayerExact(input)
    }

    override fun complete(sender: CommandSender, input: String): List<String> {
        return Bukkit.getOnlinePlayers()
            .filter { it.name.startsWith(input, ignoreCase = true) }
            .map { it.name }
    }
}