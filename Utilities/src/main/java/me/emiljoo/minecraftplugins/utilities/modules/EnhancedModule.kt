package me.emiljoo.minecraftplugins.utilities.modules

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import org.bukkit.event.Listener

abstract class EnhancedModule : Listener {
    abstract fun onEnable(plugin: EnhancedPlugin)
    abstract fun onDisable(plugin: EnhancedPlugin)
}