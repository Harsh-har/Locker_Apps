package com.example.LockerApp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.LockerApp.databinding.ActivityPin6StyleBinding

class Pin6Style : AppCompatActivity() {
    private lateinit var binding: ActivityPin6StyleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityPin6StyleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.start.setOnClickListener{
            startActivity(Intent(this@Pin6Style,Pin6Draw::class.java))
        }
        binding.cancle.setOnClickListener{
            startActivity(Intent(this@Pin6Style,PinStyle::class.java))
        }
        binding.backArrow.setOnClickListener{
            startActivity(Intent(this@Pin6Style,PinStyle::class.java))
        }

    }
}