package me.emiljoo.minecraftplugins.basic.commands

import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.commands.EnhancedCommand
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GamemodeCommand : EnhancedCommand(
    "gamemode",
    "basic.admin",
    usage = "/gamemode <mode> [player]",
    aliases = listOf("gm")
) {
    override fun onCommandExecution(sender: CommandSender, commandLabel: String, args: Array<out String>, messenger: Messenger) {
        when (args.size) {
            0 -> handleNoArgs(sender, messenger)
            1 -> handleOneArg(sender, messenger, args[0])
            2 -> handleTwoArgs(sender, args[0], args[1], messenger)
            else -> {
                messenger.toCommandSender(sender, usage)
            }
        }
    }

    private fun handleNoArgs(sender: CommandSender, messenger: Messenger) {
        if (sender is Player) {
            togglePlayerGameMode(sender)
            messenger.toCommandSender(sender, "Your game mode has been toggled!")
        } else {
            messenger.toCommandSender(sender, usage)
        }
    }

    private fun togglePlayerGameMode(player: Player) {
        val newGameMode = if (player.gameMode == GameMode.SURVIVAL) GameMode.CREATIVE else GameMode.SURVIVAL
        player.gameMode = newGameMode
    }

    private fun handleOneArg(sender: CommandSender, messenger: Messenger, arg: String) {
        if (sender is Player) {
            val newGameMode = getGameModeFromArg(arg)
            sender.gameMode = newGameMode
            messenger.toCommandSender(sender, "Your game mode has been changed to &4${newGameMode.toString().lowercase()}&r.")
        } else {
            messenger.toCommandSender(sender, "You have to specify the target!")
        }
    }

    private fun handleTwoArgs(sender: CommandSender, gameModeArg: String, playerName: String, messenger: Messenger) {
        val player = Bukkit.getPlayerExact(playerName)
        if (player == null || !player.isOnline) {
            messenger.toCommandSender(sender, "Player is not online!")
            return
        }

        val newGameMode = getGameModeFromArg(gameModeArg)
        player.gameMode = newGameMode

        if (player != sender) {
            messenger.toCommandSender(sender, "&4$playerName&r's game mode has been changed to &4${newGameMode.toString().lowercase()}&r.")
        }

        messenger.toCommandSender(player, "Your game mode has been changed to &4${newGameMode.toString().lowercase()}&r.")
        return
    }

    private fun getGameModeFromArg(arg: String): GameMode {
        return when (arg.lowercase()) {
            "1", "c", "creative" -> GameMode.CREATIVE
            "2", "a", "adventure" -> GameMode.ADVENTURE
            "3", "spec", "spectator" -> GameMode.SPECTATOR
            else -> GameMode.SURVIVAL
        }
    }

}