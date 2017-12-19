package edu.uet

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeSupport

/**
 * Dispatch events for rendering UI
 */
object GameDispatcher {
    private val dispatcher = PropertyChangeSupport(this)

    fun dispatch(event: String, oldValue: Any?, newValue: Any?) {
        dispatcher.firePropertyChange(event, oldValue, newValue)
    }

    fun listen(event: String, listener: (PropertyChangeEvent) -> Unit) {
        dispatcher.addPropertyChangeListener(event, listener)
    }
}