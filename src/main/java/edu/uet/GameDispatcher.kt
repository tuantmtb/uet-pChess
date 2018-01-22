package edu.uet

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeSupport

/**
 * Dispatch events for rendering UI
 */
object GameDispatcher {
    private var dispatcher = PropertyChangeSupport(this)

    fun fire(event: String, oldValue: Any?, newValue: Any?) {
        dispatcher.firePropertyChange(event, oldValue, newValue)
    }

    fun on(event: String, listener: (PropertyChangeEvent) -> Unit): (PropertyChangeEvent) -> Unit {
        dispatcher.addPropertyChangeListener(event, listener)
        return listener
    }

    fun unbind(event: String, listener: (PropertyChangeEvent) -> Unit) {
        dispatcher.removePropertyChangeListener(event, listener)
    }
}