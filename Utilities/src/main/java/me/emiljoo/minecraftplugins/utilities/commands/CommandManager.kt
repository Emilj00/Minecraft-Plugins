package me.emiljoo.minecraftplugins.utilities.commands

import me.emiljoo.minecraftplugins.utilities.ClassScanner
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.LogLevel
import me.emiljoo.minecraftplugins.utilities.Messenger
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.command.PluginCommand
import org.bukkit.plugin.SimplePluginManager


internal class CommandManager(private val plugin: EnhancedPlugin) {
    private val commandsPackageName: String = plugin.getPluginPackage()
    private val messenger: Messenger = plugin.messenger

    private val commandMap: CommandMap

    init {
        val commandMapField = SimplePluginManager::class.java.getDeclaredField("commandMap")
        commandMapField.setAccessible(true)
        commandMap = commandMapField.get(Bukkit.getPluginManager()) as CommandMap
    }

    fun registerCommands() {
        val classScanner = ClassScanner()

        val commandClasses = classScanner.findSubclassesOf(EnhancedCommand::class.java, commandsPackageName)
        for (commandClass in commandClasses) {
            messenger.toConsole(LogLevel.Info, "Found command at ${commandClass.`package`}.${commandClass.typeName}")

            val enhancedCommand: EnhancedCommand = commandClass.getConstructor().newInstance() as EnhancedCommand

            commandMap.register(plugin.name, enhancedCommand)
            messenger.toConsole(LogLevel.Error, "Command called ${enhancedCommand.name} is not present in plugin.yml")
        }
    }
}