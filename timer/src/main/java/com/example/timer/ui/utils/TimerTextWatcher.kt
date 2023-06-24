package com.example.timer.ui.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import java.util.regex.Pattern

open class TimerTextWatcher(
    private val editTextTimer: EditText, private val buttonStart: Button
) : TextWatcher {
    private val timeRegex = "^(\\d){2}(:([0-5]\\d)){2}$"
    private val timePattern = Pattern.compile(timeRegex)

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

        for (index in digitsOnly.indices) {
            if (index > 0 && index % 2 == 0 && index < digitsOnly.length) {
                formattedInput.append(":")
            }
            formattedInput.append(digitsOnly[index])
        }

        return formattedInput.toString()
    }

    private fun validateTimerInput(input: String) {
        val result = timePattern.matcher(input).matches()

        // 3 couple of numbers and 2 double dots
        if (input.length > 8) {
        }

        buttonStart.isEnabled = result
    }

}
