package com.example.timer.ui.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import java.util.regex.Pattern

open class TimerTextWatcher(
    private val editTextTimer: EditText, private val buttonStart: Button
) : TextWatcher {
    private val timerRegex = "^(?:[01]\\d|2[0-3]):(?:[0-5]\\d):(?:[0-5]\\d)$"
    private val timePattern = Pattern.compile(timerRegex)

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        validateTimerInput(s.toString())
    }

    override fun afterTextChanged(s: Editable?) {
        if (!s.isNullOrBlank()) {
            val input = s.toString()
            val formattedInput = formatTimerInput(input)
            editTextTimer.removeTextChangedListener(this)
            editTextTimer.setText(formattedInput)
            editTextTimer.setSelection(formattedInput.length)
            editTextTimer.addTextChangedListener(this)
        }
        validateTimerInput(s.toString())
    }

    private fun formatTimerInput(input: String): String {
        val digitsOnly = input.replace(Regex("\\D"), "")
        val formattedInput = StringBuilder()

        for (i in digitsOnly.indices) {
            if (i > 0 && i % 2 == 0 && i < digitsOnly.length) {
                formattedInput.append(":")
            }
            formattedInput.append(digitsOnly[i])
        }

        return formattedInput.toString()
    }

    private fun validateTimerInput(input: String) {
        buttonStart.isEnabled = timePattern.matcher(input).matches()
    }

}
