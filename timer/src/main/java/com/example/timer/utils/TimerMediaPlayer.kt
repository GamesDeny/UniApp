package com.example.timer.utils

import android.media.MediaPlayer
import com.example.timer.MainActivity
import com.example.timer.R

class TimerMediaPlayer private constructor() : MediaPlayer() {
    companion object {
        @Volatile
        private var instance: MediaPlayer? = null

        fun initializeMediaPlayer(mainActivity: MainActivity) {
            instance = create(mainActivity, R.raw.timer_end_sound)
            instance?.setOnCompletionListener { stopMediaPlayer() }
        }

        fun stopMediaPlayer() {
            instance?.apply {
                if (isPlaying) {
                    stop()
                }
                release()
            }
            instance = null
        }

        fun playTimerSound() {
            instance?.start()
        }
    }

}