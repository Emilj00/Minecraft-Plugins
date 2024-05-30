package me.emiljoo.minecraftplugins.utilities.events

open class EnhancedEvent<T> {
    private val listeners = mutableListOf<(T) -> Unit>()

    operator fun plusAssign(listener: (T) -> Unit) {
        listeners.add(listener)
    }

    operator fun minusAssign(listener: (T) -> Unit) {
        listeners.remove(listener)
    }

    fun invoke(eventData: T) {
        for (listener in listeners) {
            listener(eventData)
        }
    }
}
