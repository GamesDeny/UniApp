package com.example.timer.utils

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText

open class TimerTextWatcher(
    private val editText: EditText,
    private val timerValue: TimerValues
) : TextWatcher {
    private val tag = "TimerTextWatcher"

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Not necessary
    }

    override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
        // Not necessary
    }

    @SuppressLint("SetTextI18n")
    override fun afterTextChanged(editable: Editable?) {
        editText.removeTextChangedListener(this)

        val text = editable.toString()
        Log.d(tag, "Found text: $text")
        if (text.isNotBlank()) {
            val value = text.toInt()

            Log.d(tag, "Found $timerValue: $value")
            when (timerValue) {
                TimerValues.HOUR -> {
                    if (value < 10) {
                        editText.setText(value.toString())
                        editText.setSelection(text.length)
                    } else if (value > 99) {
                        editText.setText("99")
                        editText.setSelection(text.length)
                    }
                }

                TimerValues.MINUTE, TimerValues.SECOND -> {
                    if (value < 10) {
                        editText.setText(value.toString())
                        editText.setSelection(text.length)
                    } else if (value > 59) {
                        editText.setText("59")
                        editText.setSelection(text.length)
                    }
                }
            }
        }

        editText.addTextChangedListener(this)
    }

}
