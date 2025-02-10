package com.aznos.event

import kotlin.reflect.KClass

object EventManager {
    private val listeners = mutableMapOf<KClass<out Event>, MutableList<(Event) -> Unit>>()

    /**
     * Registers an event listener for the specified event type
     *
     * @param eventClass The KClass of the event
     * @param listener The listener function to call when the event is fired
     */
    fun <T : Event> registerListener(eventClass: KClass<T>, listener: (T) -> Unit) {
        val list = listeners.getOrPut(eventClass) { mutableListOf() }
        list.add(listener as (Event) -> Unit)
    }

    /**
     * Inline and reified version of [registerListener]
     */
    inline fun <reified T : Event> registerListener(noinline listener: (T) -> Unit) {
        registerListener(T::class, listener)
    }

    /**
     * Fires the given event, calling all listeners registered for its type
     *
     * @param event The event to fire
     */
    fun fireEvent(event: Event) {
        listeners[event::class]?.forEach { listener ->
            listener.invoke(event)
        }
    }
}