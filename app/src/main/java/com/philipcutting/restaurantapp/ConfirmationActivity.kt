package com.philipcutting.restaurantapp

import android.content.Context
import android.content.Intent
import android.icu.text.TimeZoneFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.format.DateUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.philipcutting.restaurantapp.databinding.ActivityConfirmationBinding
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import java.util.*

class ConfirmationActivity: AppCompatActivity() {

    companion object{
        const val TAG = "ConfirmationActivity"
        const val PREPARATION_TIME_IN_MINUTES = "PREPARATION_TIME_IN_MINUTES"
        const val TIME_ORDERED = "TIME_ORDERED"
        fun createIntent(
            context: Context,
            minutesNeeded: Int,
            timeOrdered: Instant
        ) : Intent {
            return Intent(context, ConfirmationActivity::class.java).apply {
                putExtra(PREPARATION_TIME_IN_MINUTES, minutesNeeded)
                putExtra(TIME_ORDERED, timeOrdered)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val returnedIntent = intent.extras
        val minutesNeeded: Int = returnedIntent?.get(PREPARATION_TIME_IN_MINUTES) as Int
        val orderedAtTime: Instant = returnedIntent.get(TIME_ORDERED) as Instant
        val readyAt = orderedAtTime.plusSeconds(((minutesNeeded) * 60).toLong())

        val formattedTime = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
            .withZone(ZoneId.systemDefault())

        binding.orderedAtTime.text = formattedTime.format(orderedAtTime)
        binding.preparationTimeInMin.text = "$minutesNeeded minutes"
        binding.readyAtTime.text = formattedTime.format(readyAt)

        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                binding.timer.text = DateUtils.formatElapsedTime(countdownInSeconds(readyAt))
                if(Instant.now() < readyAt) {
                    mainHandler.postDelayed(this, 1000)
                }
            }
        })

        binding.newOrderButton.setOnClickListener {
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
        }
    }

    fun countdownInSeconds(readyAtInstant: Instant) : Long {
        return readyAtInstant.epochSecond - Instant.now().epochSecond
    }
}