package com.example.LockerApp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.LockerApp.databinding.ActivityPin4DrawBinding


class Pin4Draw : AppCompatActivity() {
    private lateinit var binding: ActivityPin4DrawBinding
    private var enteredPin: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPin4DrawBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.backArrow.setOnClickListener{
            startActivity(Intent(this@Pin4Draw,Pin4Style::class.java))
        }
        binding.textView8.setOnClickListener{
            startActivity(Intent(this@Pin4Draw,PatternActivity::class.java))
        }
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
            binding.startt to "*"
        )

        numberMapping.forEach { (view, number) ->
            view.setOnClickListener {
                addNumberToPin(number)
            }
        }
    }

    private fun addNumberToPin(number: String) {
        if (enteredPin.length < 4) {
            enteredPin += number
            updateCircles()

            if (enteredPin.length == 4) {
                val intent = Intent(this, Verify4PinActivity::class.java)
                intent.putExtra("entered_pin", enteredPin)
                startActivity(intent)
            }
        }
    }

    private fun updateCircles() {
        val circles = listOf(
            binding.textView9,
            binding.imageView7,
            binding.imageView67,
            binding.imageView9

        )

        circles.forEachIndexed { index, circle ->
            if (index < enteredPin.length) {
                circle.setBackgroundResource(R.drawable.circle_filled) // filled circle drawable
            } else {
                circle.setBackgroundResource(R.drawable.circle_unfilled) // unfilled circle drawable
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Reset the pin and update circles
        enteredPin = ""
        updateCircles()  // Reset circles to be empty
    }
}
