package com.example.LockerApp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.LockerApp.databinding.ActivityPinStyleBinding


class PinStyle : AppCompatActivity() {
    private lateinit var binding: ActivityPinStyleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityPinStyleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.pattern.setOnClickListener {
            startActivity(Intent(this, Pin4Style::class.java))
        }

        binding.btn.setOnClickListener {
            startActivity(Intent(this, Pin6Style::class.java))
        }
        binding.backArrow.setOnClickListener {
            startActivity(Intent(this, LockActivity::class.java))
        }

    }
}