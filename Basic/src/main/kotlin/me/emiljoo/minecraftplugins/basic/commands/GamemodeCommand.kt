package me.emiljoo.minecraftplugins.basic.commands

import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.commands.CommandArgument
import me.emiljoo.minecraftplugins.utilities.commands.EnhancedCommand
import me.emiljoo.minecraftplugins.utilities.commands.arguments.PlayerArgument
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GamemodeCommand : EnhancedCommand(
    "gamemode",
    "basic.admin",
    usage = "/gamemode <mode> [player]",
    aliases = listOf("gm")
) {
    private class GameModeArgument : CommandArgument<GameMode> {
        override fun parse(sender: CommandSender, input: String): GameMode {
            return when (input.lowercase()) {
                "1", "c", "creative" -> GameMode.CREATIVE
                "2", "a", "adventure" -> GameMode.ADVENTURE
                "3", "spec", "spectator" -> GameMode.SPECTATOR
                else -> GameMode.SURVIVAL
            }
        }

        override fun complete(sender: CommandSender, input: String): List<String> {
            val modes = listOf("creative", "survival", "adventure", "spectator")
            return modes.filter { it.startsWith(input, ignoreCase = true) }
        }
    }

    init {
        addArgument(GameModeArgument())
        addArgument(PlayerArgument())
    }

    override fun onCommandExecution(sender: CommandSender, commandLabel: String, args: List<Any?>, messenger: Messenger) {
        when (args.size) {
            0 -> handleNoArgs(sender, messenger)
            1 -> handleOneArg(sender, messenger, args[0] as GameMode)
            2 -> handleTwoArgs(sender, args[0] as GameMode, args[1] as Player, messenger)
            else -> messenger.toCommandSender(sender, usage)
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

    private fun handleOneArg(sender: CommandSender, messenger: Messenger, gameMode: GameMode) {
        if (sender is Player) {
            sender.gameMode = gameMode
            messenger.toCommandSender(sender, "Your game mode has been changed to &4${gameMode.toString().lowercase()}&r.")
        } else {
            messenger.toCommandSender(sender, "You have to specify the target!")
        }
    }

    private fun handleTwoArgs(sender: CommandSender, gameMode: GameMode, player: Player, messenger: Messenger) {
        if (!player.isOnline) {
            messenger.toCommandSender(sender, "Player is not online!")
            return
        }

        player.gameMode = gameMode

        if (player != sender) {
            messenger.toCommandSender(sender, "&4${player.name}&r's game mode has been changed to &4${gameMode.toString().lowercase()}&r.")
        }

        messenger.toPlayer(player, "Your game mode has been changed to &4${gameMode.toString().lowercase()}&r.")
    }
}