package me.emiljoo.minecraftplugins.utilities

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.logging.Logger

enum class LogLevel {
    Info,
    Warning,
    Error,
}

fun colorize(text: String) : String {
    return ChatColor.translateAlternateColorCodes('&', text)
}

class Messenger(private val enhancedPlugin: EnhancedPlugin) {
    private val pluginLogger: Logger = enhancedPlugin.logger;
    private val pluginPrefix: String = enhancedPlugin.getPluginPrefix()

    fun toCommandSender(commandSender: CommandSender, message: String, usePrefix: Boolean = true) {
        val messageToSend = colorize("${if (usePrefix) pluginPrefix else ""} $message");
        commandSender.sendMessage(messageToSend)
    }

    fun toAllPlayers(message: String, usePrefix: Boolean = true) {
        val messageToBroadcast = colorize("${if (usePrefix) pluginPrefix else ""} $message")
        Bukkit.broadcastMessage(messageToBroadcast)
    }

    fun toPlayer(player: Player, message: String, usePrefix: Boolean = true) {
        val messageToSend = colorize("${if (usePrefix) pluginPrefix else ""} $message");
        player.sendMessage(messageToSend)
    }

    fun toConsole(logLevel: LogLevel, message: String) {
        if (logLevel > enhancedPlugin.getLogLevel()) {
            return
        }

        when (logLevel) {
            LogLevel.Info -> pluginLogger.info(colorize("$pluginPrefix &f$message"))
            LogLevel.Warning -> pluginLogger.info(colorize("$pluginPrefix &e$message"))
            LogLevel.Error -> pluginLogger.info(colorize("$pluginPrefix &c$message"))
        }
    }
}