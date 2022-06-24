package com.example.time

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.example.time.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    var timer: CountDownTimer? = null
    var player: MediaPlayer? = null

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        player = MediaPlayer.create(this, R.raw.alarm_bell)
        binding.seekbar.max = 600
        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.tvTimer.text = format((binding.seekbar.progress).toLong())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        binding.startBtn.setOnClickListener {
            startTimer()
        }
        binding.stopBtn.setOnClickListener {
            timer?.cancel()
            player?.stop()
            binding.startBtn.isClickable = true
            binding.seekbar.isEnabled = true
            binding.stopBtn.isClickable = false
            binding.startBtn.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green))
        }
    }

    private fun startTimer() {
        val millis = binding.seekbar.progress * 1000L
        timer = object : CountDownTimer(millis, 1000) {
            @SuppressLint("ResourceAsColor")
            override fun onTick(l: Long) {
                binding.imageView.setImageResource(R.drawable.bulb_off)
                binding.seekbar.progress = (l / 1000).toInt()
                binding.tvTimer.text = format((l / 1000))
                binding.startBtn.isClickable = false
                binding.seekbar.isEnabled = false
                binding.stopBtn.isClickable = true
                binding.startBtn.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey))
            }

            @SuppressLint("ResourceAsColor")
            override fun onFinish() {
                player = MediaPlayer.create(applicationContext, R.raw.alarm_bell)
                binding.imageView.setImageResource(R.drawable.bulb_on)
                binding.startBtn.isClickable = true
                binding.seekbar.isEnabled = true
                binding.stopBtn.isEnabled = false
                binding.startBtn.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green))
                player?.start()
            }
        }
        timer?.start()
    }

    fun format(sec: Long): String {
        val minutes = (sec / 60)
        val seconds = (sec % 60)
        return String.format("%02d:%02d", minutes, seconds)
    }
}