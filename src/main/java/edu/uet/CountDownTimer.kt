package edu.uet

import java.beans.PropertyChangeSupport
import java.util.*
import kotlin.concurrent.timer

class CountDownTimer(private val propertyChangeSupport: PropertyChangeSupport) {
    private var count: Int? = null
    var t: Timer? = null

    fun start() {
        count = ChessConfig.INITIAL_COUNT_DOWN
        propertyChangeSupport.firePropertyChange("COUNT_DOWN_TICK", null, count)
        t = timer(initialDelay = 1000, period = 1000, action = {
            tick()
        })

    }

    fun restart() {
        stop()
        start()
    }

    fun stop() {
        t?.cancel()
        t = null
        count = null
    }

    private fun tick() {
        val oldValue = count
        count = count!! - 1
        propertyChangeSupport.firePropertyChange("COUNT_DOWN_TICK", oldValue, count)

        if (count!! <= 0) {
            val oldVal = count
            stop()
            propertyChangeSupport.firePropertyChange("COUNT_DOWN_TIMEOUT", oldVal, count)
        }
    }
}