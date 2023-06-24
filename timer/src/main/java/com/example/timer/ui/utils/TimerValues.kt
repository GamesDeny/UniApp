package com.example.timer.ui.utils

enum class TimerValues(private val maxValue: Int) {
    HOUR(99),
    MINUTE(59),
    SECOND(59),
    ;

    fun getMaxValue(): Int {
        return maxValue;
    }

}