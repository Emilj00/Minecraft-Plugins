package me.emiljoo.minecraftplugins.basic.commands.admin

import me.emiljoo.minecraftplugins.basic.BasicPlugin
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.commands.EnhancedCommand
import me.emiljoo.minecraftplugins.utilities.commands.ICommandArgument
import org.bukkit.command.CommandSender

private enum class BasicCommandArgs {
    Reload,
}

private class BasicCommandArgument : ICommandArgument<BasicCommandArgs> {
    override fun parse(sender: CommandSender, input: String): BasicCommandArgs? {
        return try {
            BasicCommandArgs.valueOf(input.capitalize())
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override fun complete(sender: CommandSender, input: String): List<String> {
        val inputLower = input.lowercase()
        return BasicCommandArgs.entries
            .map { it.name.lowercase() }
            .filter { it.startsWith(inputLower) }
    }
}

class BasicCommand : EnhancedCommand("basic", "basic.admin", usage = "/basic <argument>") {
    init {
        addArgument(BasicCommandArgument())
    }

    override fun onCommandExecution(sender: CommandSender, commandLabel: String, args: List<Any?>, messenger: Messenger) {
        if (args.isEmpty() || args.size != 1) {
            messenger.toCommandSender(sender, usage)
            return
        }

        val basicCommandArgument: BasicCommandArgs = args[0] as BasicCommandArgs

        when (basicCommandArgument) {
            BasicCommandArgs.Reload -> handleReloadArg(sender, messenger)
        }
    }

    private fun handleReloadArg(sender: CommandSender, messenger: Messenger) {
        val plugin: BasicPlugin = EnhancedPlugin.getInstance() as BasicPlugin

        plugin.accountController.reloadUsersDatabase()
        plugin.configManager.reloadConfig()

        messenger.toCommandSender(sender, "Basic plugin reloaded")
    }
}