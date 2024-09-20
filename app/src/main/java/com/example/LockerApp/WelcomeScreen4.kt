package com.example.LockerApp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.LockerApp.databinding.ActivityWelcomeScreen4Binding

class WelcomeScreen4 : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeScreen4Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeScreen4Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backArrow.setOnClickListener{
            startActivity(Intent(this,WelcomeScreen3::class.java))
        }
        binding.button.setOnClickListener{
            startActivity(Intent(this,LockActivity::class.java))
        }

    }
}