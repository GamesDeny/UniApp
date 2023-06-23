package com.example.timer

import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.timer.ui.utils.TimerMediaPlayer
import com.example.timer.ui.utils.TimerTextWatcher
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var editTextTimer: EditText
    private lateinit var buttonStart: Button
    private var isTimerRunning = false

    private lateinit var textViewTimer: TextView
    private var countDownTimer: CountDownTimer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TimerMediaPlayer.initializeMediaPlayer(this)

        editTextTimer = findViewById(R.id.edit_text_timer)
        buttonStart = findViewById(R.id.button_start)
        textViewTimer = findViewById(R.id.text_view_title)
        editTextTimer.addTextChangedListener(TimerTextWatcher(editTextTimer, buttonStart))
        buttonStart.setOnClickListener {
            if (isTimerRunning) {
                stopTimer()
            } else {
                startTimer()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        TimerMediaPlayer.stopMediaPlayer()
    }


    private fun startTimer() {
        val input = editTextTimer.text.toString()
        val timeComponents = input.split(":")
        val hours = timeComponents[0].toLong()
        val minutes = timeComponents[1].toLong()
        val seconds = timeComponents[2].toLong()

        val totalMilliseconds = TimeUnit.HOURS.toMillis(hours) +
                TimeUnit.MINUTES.toMillis(minutes) +
                TimeUnit.SECONDS.toMillis(seconds)

        countDownTimer = object : CountDownTimer(totalMilliseconds, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = String.format(
                    "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                )
                textViewTimer.text = timeLeft
            }

            override fun onFinish() {
                TimerMediaPlayer.playTimerSound()
                textViewTimer.text = getString(R.string.timer_executed)
                isTimerRunning = false
                buttonStart.text = getString(R.string.button_start_label)
                editTextTimer.isEnabled = true
            }

        }

        countDownTimer?.start()
        isTimerRunning = true
        buttonStart.text = getString(R.string.button_stop_label)
        editTextTimer.isEnabled = false
    }

    private fun stopTimer() {
        countDownTimer?.cancel()
        textViewTimer.text = getString(R.string.timer_interrupted)
        isTimerRunning = false
        buttonStart.text = getString(R.string.button_start_label)
        editTextTimer.isEnabled = true
    }

}
