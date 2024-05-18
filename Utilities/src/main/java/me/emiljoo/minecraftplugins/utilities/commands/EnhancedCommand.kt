package me.emiljoo.minecraftplugins.utilities.commands

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.LogLevel
import me.emiljoo.minecraftplugins.utilities.Messenger
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
    protected var enhancedPlugin: EnhancedPlugin? = null

    fun setPlugin(plugin: EnhancedPlugin) {
        this.enhancedPlugin = plugin
    }

    fun enableCommand() {
        isCommandEnabled = true
    }

    fun disableCommand() {
        isCommandEnabled = false
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

        onCommandExecution(sender, commandLabel, args, messenger)

        return true
    }

    abstract fun onCommandExecution(sender: CommandSender, commandLabel: String, args: Array<out String>, messenger: Messenger)

    override fun setName(name: String): Boolean {
        messenger.toConsole(LogLevel.Warning, "$4You can't change command's name!")
        return false
    }
}
