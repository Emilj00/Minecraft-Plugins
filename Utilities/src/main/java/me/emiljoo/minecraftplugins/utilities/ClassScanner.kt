package me.emiljoo.minecraftplugins.utilities

import org.reflections.Reflections

class ClassScanner {
    fun findSubclassesOf(abstractClass: Class<*>, packageName: String): Set<Class<*>> {
        val reflections = Reflections(packageName)
        return reflections.getSubTypesOf(abstractClass)
    }
}
