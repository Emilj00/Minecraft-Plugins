package me.emiljoo.minecraftplugins.utilities.commands

import me.emiljoo.minecraftplugins.utilities.ClassScanner
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.LogLevel
import me.emiljoo.minecraftplugins.utilities.Messenger
import org.bukkit.command.PluginCommand

class CommandManager(private val plugin: EnhancedPlugin) {
    private val commandsPackageName: String = plugin.getPluginPackage()
    private val messenger: Messenger = plugin.messenger

    fun registerCommands() {
        val classScanner = ClassScanner()

        val commandClasses = classScanner.findSubclassesOf(EnhancedCommand::class.java, commandsPackageName)
        for (commandClass in commandClasses) {
            messenger.toConsole(LogLevel.Info, "Found command at ${commandClass.`package`}.${commandClass.typeName}")

            val enhancedCommand: EnhancedCommand = commandClass.newInstance() as EnhancedCommand

            val command: PluginCommand? = plugin.getCommand(enhancedCommand.commandName)

            if (command == null) {
                messenger.toConsole(LogLevel.Error, "Command called ${enhancedCommand.commandName} is not present in plugin.yml")
                return
            }

            command.setExecutor(enhancedCommand)
        }
    }
}