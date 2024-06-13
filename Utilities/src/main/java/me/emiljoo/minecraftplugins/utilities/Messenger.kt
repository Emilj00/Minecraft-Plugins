package me.emiljoo.minecraftplugins.utilities

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

enum class LogLevel {
    Info,
    Warning,
    Error,
}

class Messenger(private val enhancedPlugin: EnhancedPlugin) {
    companion object {
        fun colorize(text: String): String = ChatColor.translateAlternateColorCodes('&', text)
    }

    private val pluginBukkitSender: ConsoleCommandSender = Bukkit.getConsoleSender()
    private val pluginPrefix: String = enhancedPlugin.getPluginPrefix()

    fun toCommandSender(commandSender: CommandSender, message: String, usePrefix: Boolean = true) {
        val messageToSend = colorize("${if (usePrefix) "$pluginPrefix " else ""}$message")
        commandSender.sendMessage(messageToSend)
    }

    fun toAllPlayers(message: String, usePrefix: Boolean = true) {
        val messageToSend = colorize("${if (usePrefix) "$pluginPrefix " else ""}$message")
        Bukkit.getOnlinePlayers().forEach { player: Player? -> player?.sendMessage(messageToSend) }
    }

    fun toPlayer(player: Player, message: String, usePrefix: Boolean = true) {
        val messageToSend = colorize("${if (usePrefix) "$pluginPrefix " else ""}$message")
        player.sendMessage(messageToSend)
    }

    fun toConsole(logLevel: LogLevel, message: String) {
        if (enhancedPlugin.getLogLevel() > logLevel) {
            return
        }

        when (logLevel) {
            LogLevel.Info -> pluginBukkitSender.sendMessage(colorize("$pluginPrefix &f$message"))
            LogLevel.Warning -> pluginBukkitSender.sendMessage(colorize("$pluginPrefix &e$message"))
            LogLevel.Error -> pluginBukkitSender.sendMessage(colorize("$pluginPrefix &c$message"))
        }
    }
}
