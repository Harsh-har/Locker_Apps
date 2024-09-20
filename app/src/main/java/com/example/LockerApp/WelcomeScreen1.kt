package com.example.LockerApp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.LockerApp.databinding.ActivityWelcomeScreen1Binding

class WelcomeScreen1 : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeScreen1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeScreen1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener{
            startActivity(Intent(this,WelcomeScreen2::class.java))
        }
    }
}
