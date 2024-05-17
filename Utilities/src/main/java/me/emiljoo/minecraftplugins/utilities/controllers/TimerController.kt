package me.emiljoo.minecraftplugins.utilities.controllers

import me.emiljoo.minecraftplugins.utilities.EnhancedPlugin
import me.emiljoo.minecraftplugins.utilities.data.types.TimerPlayerDataEntry
import me.emiljoo.minecraftplugins.utilities.modules.EnhancedModule
import org.bukkit.scheduler.BukkitRunnable

class TimerController() {
    private var isTimersTicking: Boolean = false
    private val timersList: MutableList<TimerPlayerDataEntry> = ArrayList()

    private val runnable = object : BukkitRunnable() {
        override fun run() {
            timersList.forEach { timer -> timer.tick() }
        }
    }

    fun startTimers(plugin: EnhancedPlugin) {
        isTimersTicking = true
        runnable.runTaskTimer(plugin, 0, 20)
    }

    fun stopTimers() {
        isTimersTicking = false
        runnable.cancel()
    }

    fun addTimer(timer: TimerPlayerDataEntry) {
        timersList.add(timer)
    }

    fun removeTimer(timer: TimerPlayerDataEntry) {
        timersList.remove(timer)
    }
}