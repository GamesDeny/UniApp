package com.example.timer

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.timer.ui.utils.TimerMediaPlayer

class MainActivity : AppCompatActivity() {
    private lateinit var textViewTimer: TextView
    private lateinit var editTextHours: EditText
    private lateinit var editTextMinutes: EditText
    private lateinit var editTextSeconds: EditText
    private lateinit var buttonStartTimer: Button
    private lateinit var buttonStopTimer: Button
    private lateinit var countDownTimer: CountDownTimer
    private val TAG = "MainActivity - "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TimerMediaPlayer.initializeMediaPlayer(this)
        textViewTimer = findViewById(R.id.text_view_timer)
        editTextHours = findViewById(R.id.edit_text_hours)
        editTextMinutes = findViewById(R.id.edit_text_minutes)
        editTextSeconds = findViewById(R.id.edit_text_seconds)
        buttonStartTimer = findViewById(R.id.button_start_timer)
        buttonStopTimer = findViewById(R.id.button_stop_timer)

        buttonStartTimer.setOnClickListener {
            Log.d(TAG, "Setting listener for start button")
            val hours = getTimeOrZero(editTextHours)
            val minutes = getTimeOrZero(editTextMinutes)
            val seconds = getTimeOrZero(editTextSeconds)

            val totalMilliseconds = ((hours * 60 * 60) + (minutes * 60) + seconds) * 1000L

            countDownTimer = object : CountDownTimer(totalMilliseconds, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val remainingSeconds = millisUntilFinished / 1000
                    val displayHours = remainingSeconds / 3600
                    val displayMinutes = (remainingSeconds % 3600) / 60
                    val displaySeconds = remainingSeconds % 60

                    val timerText = String.format(
                        "%02d:%02d:%02d",
                        displayHours,
                        displayMinutes,
                        displaySeconds
                    )
                    textViewTimer.text = timerText
                }

                override fun onFinish() {
                    textViewTimer.text = getString(R.string.timer_initial_value)
                    TimerMediaPlayer.playTimerSound()
                    // Play sound and show notification here
                }
            }

            countDownTimer.start()
        }

        buttonStopTimer.setOnClickListener {
            Log.d(TAG, "Setting listener for stop button")
            countDownTimer.cancel()
            textViewTimer.text = getString(R.string.timer_initial_value)
        }

    }

    private fun getTimeOrZero(editText: EditText): Int {
        return editText.text.toString().toIntOrNull() ?: 0
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }

}
