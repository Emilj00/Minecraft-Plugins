package me.emiljoo.minecraftplugins.utilities.commands

import me.emiljoo.minecraftplugins.utilities.ClassScanner
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.LogLevel
import me.emiljoo.minecraftplugins.utilities.Messenger
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.plugin.SimplePluginManager
import java.util.HashMap


class CommandManager() {
    private val commandMap: CommandMap
    private val commandRegistry: HashMap<String, EnhancedCommand> = HashMap()

    init {
        val commandMapField = SimplePluginManager::class.java.getDeclaredField("commandMap")
        commandMapField.setAccessible(true)
        commandMap = commandMapField.get(Bukkit.getPluginManager()) as CommandMap
    }

    fun registerCommands(plugin: EnhancedPlugin) {
        val commandsPackageName: String = plugin.getPluginPackage()
        val classScanner = ClassScanner()

        val commandClasses = classScanner.findSubclassesOf(EnhancedCommand::class.java, commandsPackageName)
        for (commandClass in commandClasses) {
            val enhancedCommand: EnhancedCommand = commandClass.getConstructor().newInstance() as EnhancedCommand

            commandMap.register(plugin.name, enhancedCommand)
            commandRegistry[enhancedCommand.name] = enhancedCommand
        }
    }

    fun getCommand(commandName: String): EnhancedCommand? {
        val command: EnhancedCommand? = commandRegistry[commandName]

        if (command == null) {
            EnhancedPlugin.getMessenger().toConsole(LogLevel.Error, "Command \"$commandName\" doesn't exist")
        }

        return commandRegistry[commandName]
    }
}