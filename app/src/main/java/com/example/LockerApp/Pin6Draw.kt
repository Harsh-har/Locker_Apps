package com.example.LockerApp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.LockerApp.databinding.ActivityPin6DrawBinding


class Pin6Draw : AppCompatActivity() {
    private lateinit var binding: ActivityPin6DrawBinding
    private var enteredPin: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPin6DrawBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.backArrow.setOnClickListener{
            startActivity(Intent(this@Pin6Draw,Pin6Style::class.java))
        }
        binding.textView8.setOnClickListener{
            startActivity(Intent(this@Pin6Draw,PatternActivity::class.java))
        }

        val numberMapping = mapOf(
            R.id.imageView11 to "1",
            R.id.imageView13 to "2",
            R.id.imageView14 to "3",
            R.id.imageView12 to "4",
            R.id.imageView15 to "5",
            R.id.six to "6",
            R.id.imageView16 to "7",
            R.id.imageView17 to "8",
            R.id.imageView18 to "9",
            R.id.imageView19 to "0",
            R.id.startt to "*"
        )

        numberMapping.forEach { (viewId, number) ->
            findViewById<ImageView>(viewId).setOnClickListener {
                addNumberToPin(number)
            }
        }
    }

    private fun addNumberToPin(number: String) {
        if (enteredPin.length < 6) {
            enteredPin += number
            updateCircles()

            if (enteredPin.length == 6) {
                // Launch the next activity when 6 digits are entered
                val intent = Intent(this, Verify6PinActivity::class.java)
                intent.putExtra("entered_pin", enteredPin)
                startActivity(intent)
            }
        }
    }

    private fun updateCircles() {
        val circleIds = listOf(

            R.id.textView9,
            R.id.imageView7,
            R.id.imageView67,
            R.id.imageView9,
            R.id.imageView95,
            R.id.imageView94,
        )


        circleIds.forEachIndexed { index, circleId ->
            val circle = findViewById<ImageView>(circleId)
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
