package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var tvTimer: TextView
    private lateinit var btnStartPause: Button
    private lateinit var btnReset: Button

    private var isRunning = false        // 스톱워치 동작 여부
    private var startTime = 0L           // 시작 시각
    private var elapsedTime = 0L         // 누적 시간
    private val handler = Handler(Looper.getMainLooper()) // UI 스레드에서 동작

    // 0.01초마다 실행되는 Runnable
    private val updateRunnable = object : Runnable {
        override fun run() {
            val currentTime = System.currentTimeMillis()
            val totalTime = elapsedTime + (currentTime - startTime)
            tvTimer.text = formatTime(totalTime)
            handler.postDelayed(this, 10) // 10ms마다 갱신
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTimer = findViewById(R.id.tvTimer)
        btnStartPause = findViewById(R.id.btnStartPause)
        btnReset = findViewById(R.id.btnReset)

        // 시작/일시정지 버튼
        btnStartPause.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }

        // 리셋 버튼
        btnReset.setOnClickListener {
            resetTimer()
        }
    }

    private fun startTimer() {
        startTime = System.currentTimeMillis()
        handler.post(updateRunnable)
        btnStartPause.text = "일시정지"
        isRunning = true
    }

    private fun pauseTimer() {
        elapsedTime += System.currentTimeMillis() - startTime
        handler.removeCallbacks(updateRunnable)
        btnStartPause.text = "시작"
        isRunning = false
    }

    private fun resetTimer() {
        handler.removeCallbacks(updateRunnable)
        startTime = 0L
        elapsedTime = 0L
        tvTimer.text = "00:00:00"
        btnStartPause.text = "시작"
        isRunning = false
    }

    private fun formatTime(ms: Long): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val milliseconds = (ms % 1000) / 10
        return String.format("%02d:%02d:%02d", minutes, seconds, milliseconds)
    }
}