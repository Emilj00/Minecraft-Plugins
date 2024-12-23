package me.emiljoo.minecraftplugins.basic.events

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerLoginEvent(val player: Player) : Event() {
    companion object {
        private val eventHandlers = HandlerList()

        @JvmStatic
        fun getEventHandlers(): HandlerList {
            return eventHandlers
        }
    }

    override fun getHandlers(): HandlerList {
        return getEventHandlers()
    }
}