package com.philipcutting.restaurantapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import androidx.appcompat.app.AppCompatActivity
import com.philipcutting.restaurantapp.databinding.ActivityConfirmationBinding
import java.time.Instant


class ConfirmationActivity: AppCompatActivity() {

    companion object{
        const val PICKUP_INSTANT = "PICKUP_INSTANT"

        fun createIntent(
            context: Context,
            instantOrderWillBeReady: Long
        ) : Intent {
            return Intent(context, ConfirmationActivity::class.java).apply {
                putExtra(PICKUP_INSTANT, instantOrderWillBeReady.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val instantOrderWillBeReady: Long = intent.getStringExtra(PICKUP_INSTANT)?.toLong() ?: 0L
        val now: Long = Instant.now().epochSecond
        val minutesLeft = instantOrderWillBeReady.minus(now)
        binding.timeLeftLabel.text = DateUtils.formatElapsedTime(minutesLeft)
    }
}