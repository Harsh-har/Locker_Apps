package com.example.LockerApp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.LockerApp.databinding.ActivityWelcomeScreen2Binding


class WelcomeScreen2 : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeScreen2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeScreen2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener{
            startActivity(Intent(this,WelcomeScreen3::class.java))
        }
        binding.skip.setOnClickListener{
            startActivity(Intent(this,LockActivity::class.java))
        }
        binding.backarrow.setOnClickListener{
            startActivity(Intent(this,WelcomeScreen1::class.java))
        }
    }
}