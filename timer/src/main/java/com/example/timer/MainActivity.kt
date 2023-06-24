package com.example.timer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.timer.utils.NotificationHandler
import com.example.timer.utils.TimerMediaPlayer
import com.example.timer.utils.TimerTextWatcher
import com.example.timer.utils.TimerValues


class MainActivity : AppCompatActivity() {
    /* Local section */
    private val tag = "MainActivity"
    private lateinit var textViewTimer: TextView
    private lateinit var notificationHandler: NotificationHandler

    /* Input section */
    private lateinit var editTextHours: EditText
    private lateinit var editTextMinutes: EditText
    private lateinit var editTextSeconds: EditText

    /* Button section */
    private lateinit var buttonStartTimer: Button
    private lateinit var buttonStopTimer: Button
    private lateinit var buttonPauseTimer: Button

    /* Timer section */
    private var remainingTime: Long = 0
    private lateinit var countDownTimer: CountDownTimer
    private var isTimerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationHandler = NotificationHandler(this)

        TimerMediaPlayer.initializeMediaPlayer(this)
        textViewTimer = findViewById(R.id.text_view_timer)

        registerNotificationChannel()
        initializeText()
        initializeStartButton()
        initializePauseButton()
        initializeStopButton()
    }

    private fun registerNotificationChannel() {
        val name = getString(R.string.channel_name)
        val description = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(getString(R.string.channel_name), name, importance)
        channel.description = description

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isTimerRunning) {
            countDownTimer.cancel()
        }
        TimerMediaPlayer.stopMediaPlayer()
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.deleteNotificationChannel(getString(R.string.channel_name))
    }

    private fun initializeStopButton() {
        buttonStopTimer = findViewById(R.id.button_stop_timer)
        buttonStopTimer.setOnClickListener {
            if (isTimerRunning) {
                isTimerRunning = false
            }

            remainingTime = 0
            countDownTimer.cancel()
            buttonStartTimer.isEnabled = true
            textViewTimer.text = getString(R.string.timer_initial_value)
            buttonStopTimer.text = getString(R.string.button_stop_label)
            getNotification()
        }
    }

    private fun initializePauseButton() {
        buttonPauseTimer = findViewById(R.id.button_pause_timer)
        buttonPauseTimer.setOnClickListener {
            if (isTimerRunning) {
                countDownTimer.cancel()
                isTimerRunning = false
            } else {
                countDownTimer = getCountDownTimer(remainingTime)
                countDownTimer.start()
            }
        }
    }

    private fun initializeStartButton() {
        buttonStartTimer = findViewById(R.id.button_start_timer)
        buttonStartTimer.setOnClickListener {
            if (!isTimerRunning) {
                buttonStartTimer.isEnabled = false
                buttonStopTimer.text = getString(R.string.button_reset_label)

                Log.d(tag, "Setting listener for start button")
                countDownTimer = getCountDownTimer(getTotalMilliseconds())

                isTimerRunning = true
                countDownTimer.start()
            }
        }
    }

    private fun getCountDownTimer(totalMilliseconds: Long) =
        object : CountDownTimer(totalMilliseconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
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
                buttonStartTimer.isEnabled = true
                isTimerRunning = false
                getNotification()
            }
        }

    private fun initializeText() {
        editTextHours = findViewById(R.id.edit_text_hours)
        editTextMinutes = findViewById(R.id.edit_text_minutes)
        editTextSeconds = findViewById(R.id.edit_text_seconds)
        editTextHours.addTextChangedListener(TimerTextWatcher(editTextHours, TimerValues.HOUR))
        editTextMinutes.addTextChangedListener(
            TimerTextWatcher(
                editTextMinutes,
                TimerValues.MINUTE
            )
        )
        editTextSeconds.addTextChangedListener(
            TimerTextWatcher(
                editTextSeconds,
                TimerValues.SECOND
            )
        )
    }

    private fun getTotalMilliseconds(): Long {
        val hours = getTimeOrZero(editTextHours)
        var minutes = getTimeOrZero(editTextMinutes)
        var seconds = getTimeOrZero(editTextSeconds)

        if (minutes > 59) {
            minutes = 59
        }
        if (seconds > 59) {
            seconds = 59
        }

        return ((hours * 60 * 60) + (minutes * 60) + seconds) * 1000L
    }

    private fun getTimeOrZero(editText: EditText): Int {
        return editText.text.toString().toIntOrNull() ?: 0
    }

    private fun getNotification() {
        return notificationHandler.createNotification(getString(R.string.default_notification_message))
    }

}
