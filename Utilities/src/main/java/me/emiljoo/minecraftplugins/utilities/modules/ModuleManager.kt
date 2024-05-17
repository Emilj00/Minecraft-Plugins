package me.emiljoo.minecraftplugins.utilities.modules

import me.emiljoo.minecraftplugins.utilities.ClassScanner
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.LogLevel
import me.emiljoo.minecraftplugins.utilities.Messenger
import org.bukkit.Bukkit
import org.bukkit.plugin.PluginManager

class ModuleManager(private val plugin: EnhancedPlugin) {
    private val modulesPackageName: String = plugin.getPluginPackage()
    private val pluginManager: PluginManager = Bukkit.getPluginManager()

    private val modules: List<EnhancedModule> = ArrayList()
    private val messenger: Messenger = plugin.messenger

    fun registerModules() {
        val classScanner = ClassScanner()
        val modules = classScanner.findSubclassesOf(EnhancedModule::class.java, modulesPackageName)

        messenger.toConsole(LogLevel.Error, "-------------------------------- ${modules.count()}")
        for (module in modules) {
            messenger.toAllPlayers(module.toString())
            val moduleInstance: EnhancedModule = module.getConstructor().newInstance() as EnhancedModule

            pluginManager.registerEvents(moduleInstance, plugin)
            moduleInstance.onEnable(plugin)
        }
    }

    fun onPluginStopped() {
        modules.forEach { module -> module.onDisable(plugin) }
    }
}