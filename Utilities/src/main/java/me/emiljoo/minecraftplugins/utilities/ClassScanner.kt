package me.emiljoo.minecraftplugins.utilities

import me.emiljoo.minecraftplugins.utilities.modules.EnhancedModule
import org.reflections.Reflections

class ClassScanner {
    fun findSubclassesOf(abstractClass: Class<*>, packageName: String): MutableSet<out Class<*>> {
        val reflections = Reflections(packageName)
        return reflections.getSubTypesOf(abstractClass)
    }
}
