package me.emiljoo.minecraftplugins.utilities.modules

import me.emiljoo.minecraftplugins.utilities.ClassScanner
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.data.PlayerDataRegistererModule
import org.bukkit.Bukkit
import org.bukkit.plugin.PluginManager

class ModuleManager() {
    private val pluginManager: PluginManager = Bukkit.getPluginManager()
    private val modules: MutableList<EnhancedModule> = mutableListOf()

    fun registerModules(plugin: EnhancedPlugin) {
        val modulesPackageName: String = plugin.getPluginPackage()

        val classScanner = ClassScanner()
        val scannedModules: MutableSet<out Class<*>> = classScanner.findSubclassesOf(EnhancedModule::class.java, modulesPackageName)

        val modulesToInstantiate: MutableSet<Class<*>> = mutableSetOf()
        modulesToInstantiate.addAll(scannedModules)

        modulesToInstantiate.add(PlayerDataRegistererModule::class.java)

        for (module in modulesToInstantiate.iterator()) {
            val moduleInstance: EnhancedModule = module.getConstructor().newInstance() as EnhancedModule
            modules.add(moduleInstance)

            pluginManager.registerEvents(moduleInstance, plugin)
        }
    }

    fun onPluginStarted(plugin: EnhancedPlugin) {
        modules.forEach { module -> module.onEnable(plugin) }
    }

    fun onPluginStopped(plugin: EnhancedPlugin) {
        modules.forEach { module -> module.onDisable(plugin) }
    }
}