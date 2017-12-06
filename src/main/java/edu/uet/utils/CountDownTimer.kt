package edu.uet.utils

import edu.uet.ChessConfig
import java.util.*
import kotlin.concurrent.timer

class CountDownTimer(private val onTick: ((oldValue: Int?, newValue: Int?) -> Unit)? = null, private val onTimeOut: (() -> Unit)? = null) {
    private var countDown: Int? = null
    private var _timer: Timer? = null

    fun start() {
        countDown = ChessConfig.COUNT_DOWN
        onTick?.invoke(null, countDown)
        _timer = timer(daemon = true, initialDelay = 1000, period = 1000, action = {
            tick()
        })

    }

    fun restart() {
        stop()
        start()
    }

    fun stop() {
        _timer?.cancel()
        _timer = null
        countDown = null
    }

    private fun tick() {
        val oldValue = countDown
        countDown = countDown!! - 1
        onTick?.invoke(oldValue, countDown)

        if (countDown!! <= 0) {
            stop()
            onTimeOut?.invoke()
        }
    }
}