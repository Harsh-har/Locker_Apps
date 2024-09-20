package com.example.LockerApp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.LockerApp.databinding.ActivityVerify4PinBinding

class Verify4PinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerify4PinBinding
    private var reEnteredPin: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerify4PinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textView8.setOnClickListener{
            startActivity(Intent(this@Verify4PinActivity,Pin4Draw::class.java))
        }
        binding.backArrow.setOnClickListener{
            startActivity(Intent(this@Verify4PinActivity,Pin4Draw::class.java))
        }

        val originalPin = intent.getStringExtra("entered_pin") ?: ""

        // Similar logic to handle the number buttons
        val numberMapping = mapOf(
            binding.imageView11 to "1",
            binding.imageView13 to "2",
            binding.imageView14 to "3",
            binding.imageView12 to "4",
            binding.imageView15 to "5",
            binding.six to "6",
            binding.imageView16 to "7",
            binding.imageView17 to "8",
            binding.imageView18 to "9",
            binding.imageView19 to "0",
            binding.imageView4 to "*"
        )

        numberMapping.forEach { (view, number) ->
            view.setOnClickListener {
                addNumberToReEnteredPin(number, originalPin)
            }
        }
    }

    private fun addNumberToReEnteredPin(number: String, originalPin: String) {
        if (reEnteredPin.length < 4) {
            reEnteredPin += number
            updateCircles()

            if (reEnteredPin.length == 4) {
                if (reEnteredPin == originalPin) {
                    // PINs match, proceed further
                    Toast.makeText(this, " Correct PIN ", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, AppLockFreeActivity::class.java))
                } else {
                    // PINs don't match, show error and clear the re-entered PIN
                    Toast.makeText(this, "PINs don't match.Please  Try again.", Toast.LENGTH_SHORT).show()
                    reEnteredPin = ""
                    updateCircles()  // Reset circles to be empty
                }
            }
        }
    }

    private fun updateCircles() {
        val circles = listOf(
            binding.textView9,
            binding.imageView7,
            binding.imageView9,
            binding.imageView8
        )

        circles.forEachIndexed { index, circle ->
            if (index < reEnteredPin.length) {
                circle.setBackgroundResource(R.drawable.circle_filled) // filled circle drawable
            } else {
                circle.setBackgroundResource(R.drawable.circle_unfilled) // unfilled circle drawable
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Reset the pin and update circles
        reEnteredPin = ""
        updateCircles()  // Reset circles to be empty
    }
}
