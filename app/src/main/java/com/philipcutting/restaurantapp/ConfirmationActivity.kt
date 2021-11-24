package com.philipcutting.restaurantapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.icu.text.TimeZoneFormat
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.format.DateUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
        const val CHANNEL_ID = "restaurant_app_id"
        const val CHANNEL_NAME = "restaurant_app_channel"
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

    private var notified = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()

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
                val remainingTime: Long = countdownInSeconds(readyAt)
                binding.timer.text = DateUtils.formatElapsedTime(remainingTime)

                if (remainingTime > 0) {
                    if(!notified && remainingTime < (60 * 10)) {
                        //10 min notification
                        callNotification(remainingTime)
                    }
                    mainHandler.postDelayed(this, 1000)
                }
            }
        })

        binding.newOrderButton.setOnClickListener {
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
        }
    }

    private fun callNotification(remainingTime: Long) {
        notified = true
        val notificationId = remainingTime.toInt()
        val builder = NotificationCompat.Builder(this@ConfirmationActivity, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_food_bank_24)
            .setContentTitle("Food ready in ${DateUtils.formatElapsedTime(remainingTime)}")
            .setContentText(
                "Get ready to pick up your food.  It will be read in ${
                    DateUtils.formatElapsedTime(
                        remainingTime
                    )
                }"
            )
            .setPriority(NotificationCompat.PRIORITY_MAX)

        with(NotificationManagerCompat.from(this@ConfirmationActivity)) {
            notify(notificationId, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val name = CHANNEL_NAME
        val descriptionText = "Channel for Restaurant pickup notifications"
        val importance: Int = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun countdownInSeconds(readyAtInstant: Instant) : Long {
        return readyAtInstant.epochSecond - Instant.now().epochSecond
    }
}