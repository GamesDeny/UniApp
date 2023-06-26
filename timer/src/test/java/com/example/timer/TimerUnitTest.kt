package com.example.timer

import com.example.timer.utils.TimerUtils
import com.example.timer.utils.TimerValues
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.stream.IntStream

class TimerUnitTest {

    private val testCases = HashMap<TimerValues, Map<String, Int>>()

    @Before
    fun initializeCases() {
        TimerValues.values().forEach { tv ->
            val map = HashMap<String, Int>()

            IntStream.range(50, 150).forEach { i ->
                map[String.format("%d", i)] = when (tv) {
                    TimerValues.HOUR -> when (i > 99) {
                        true -> TimerValues.HOUR.getMaxValue()
                        else -> i
                    }

                    TimerValues.MINUTE, TimerValues.SECOND -> when (i > 59) {
                        true -> TimerValues.SECOND.getMaxValue()
                        else -> i
                    }
                }
            }

            testCases[tv] = map
        }
    }

    @Test
    fun getMilliSecondsForHour_success() {
        val cases = testCases[TimerValues.HOUR]
        cases?.forEach { entry ->
            val result = TimerUtils.getMilliSecondsForTimeUnit(entry.key, TimerValues.HOUR)

            assertEquals(entry.value, result)
        }
    }

    @Test
    fun getMilliSecondsForMinute_success() {
        val cases = testCases[TimerValues.MINUTE]
        cases?.forEach { entry ->
            val result = TimerUtils.getMilliSecondsForTimeUnit(entry.key, TimerValues.MINUTE)

            assertEquals(entry.value, result)
        }
    }

    @Test
    fun getMilliSecondsForSecond_success() {
        val cases = testCases[TimerValues.SECOND]
        cases?.forEach { entry ->
            val result = TimerUtils.getMilliSecondsForTimeUnit(entry.key, TimerValues.SECOND)

            assertEquals(entry.value, result)
        }
    }

    @Test
    fun getTotalMillis_success() {
        val n1 = 10;
        val n2 = 20;
        val n3 = 30;
        val expected = 37230000L

        val result = TimerUtils.getTotalMilliSeconds(n1, n2, n3)

        assertEquals(expected, result)
    }

    @Test
    fun getTimeOrZero_success() {
        assertEquals(0, TimerUtils.getTimeOrZero(null))
        assertEquals(0, TimerUtils.getTimeOrZero(""))
        assertEquals(0, TimerUtils.getTimeOrZero("   "))
        assertEquals(0, TimerUtils.getTimeOrZero("0"))
        assertEquals(10, TimerUtils.getTimeOrZero("10"))
        assertEquals(1000, TimerUtils.getTimeOrZero("1000"))
    }

}