package me.emiljoo.minecraftplugins.utilities.commands

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.LogLevel
import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.commands.arguments.StringArgument
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

abstract class EnhancedCommand(
    commandName: String,
    permission: String,
    description: String = "",
    usage: String = "",
    aliases: List<String> = listOf(),
    private var isCommandEnabled: Boolean = true
) :
    Command(commandName) {

    init {
        this.permission = permission
        this.description = description
        this.usage = usage.takeIf { it.isNotEmpty() } ?: "/$commandName"
        this.aliases = aliases
    }

    private val messenger: Messenger = EnhancedPlugin.getMessenger()
    private val arguments: MutableList<ICommandArgument<*>> = mutableListOf()

    fun enableCommand() {
        isCommandEnabled = true
    }

    fun disableCommand() {
        isCommandEnabled = false
    }

    override fun setName(name: String): Boolean {
        messenger.toConsole(LogLevel.Warning, "&4You can't change command's name!")
        return false
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (!isCommandEnabled) {
            messenger.toCommandSender(sender, "&l&4This command is disabled!")
            return true
        }

        if (!sender.hasPermission(permission.toString())) {
            messenger.toCommandSender(sender, "&l&4You have no permission!")
            return true
        }

        val parsedArgs: MutableList<Any?> = mutableListOf()

        for (index in arguments.indices) {
            if (index < args.size) {
                parsedArgs.add(arguments[index].parse(sender, args[index]))
            }
        }

        if (args.size > arguments.size) {
            for (index in arguments.size until args.size) {
                val stringArgument = StringArgument()
                parsedArgs.add(stringArgument.parse(sender, args[index]))
            }
        }

        onCommandExecution(sender, commandLabel, parsedArgs, messenger)

        return true
    }

    protected abstract fun onCommandExecution(sender: CommandSender, commandLabel: String, args: List<Any?>, messenger: Messenger)

    override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): List<String> {
        val argumentIndex: Int = args.size - 1
        val argument: ICommandArgument<*> = arguments.getOrNull(argumentIndex) ?: return emptyList()

        return argument.complete(sender, args[argumentIndex])
    }

    protected fun addArgument(argument: ICommandArgument<*>) {
        arguments.add(argument)
    }
}


