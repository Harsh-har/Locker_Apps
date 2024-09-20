package com.example.LockerApp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.LockerApp.databinding.ActivityWelcomeScreen3Binding

class WelcomeScreen3 : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeScreen3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeScreen3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.skipButton.setOnClickListener{
            startActivity(Intent(this,LockActivity::class.java))
        }
        binding.button.setOnClickListener{
            startActivity(Intent(this,WelcomeScreen4::class.java))
        }
        binding.backArrow.setOnClickListener{
            startActivity(Intent(this,WelcomeScreen2::class.java))
        }
    }
}