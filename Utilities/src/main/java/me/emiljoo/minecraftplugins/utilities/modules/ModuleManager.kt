package me.emiljoo.minecraftplugins.utilities.modules

import me.emiljoo.minecraftplugins.utilities.ClassScanner
import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.Messenger
import me.emiljoo.minecraftplugins.utilities.data.PlayerDataRegistererModule
import org.bukkit.Bukkit
import org.bukkit.plugin.PluginManager

class ModuleManager(private val plugin: EnhancedPlugin) {
    private val modulesPackageName: String = plugin.getPluginPackage()
    private val pluginManager: PluginManager = Bukkit.getPluginManager()

    private val modules: List<EnhancedModule> = ArrayList()
    private val messenger: Messenger = plugin.messenger

    fun registerModules() {
        val classScanner = ClassScanner()
        val scannedModules: MutableSet<out Class<*>> = classScanner.findSubclassesOf(EnhancedModule::class.java, modulesPackageName)

        val modules: MutableSet<Class<*>> = mutableSetOf()
        modules.addAll(scannedModules)

        modules.add(PlayerDataRegistererModule::class.java)

        for (module in modules.iterator()) {
            val moduleInstance: EnhancedModule = module.getConstructor().newInstance() as EnhancedModule

            pluginManager.registerEvents(moduleInstance, plugin)
            moduleInstance.onEnable(plugin)
        }
    }

    fun onPluginStopped() {
        modules.forEach { module -> module.onDisable(plugin) }
    }
}