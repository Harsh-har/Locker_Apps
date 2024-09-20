package com.example.LockerApp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.LockerApp.databinding.ActivityPatterndetails5Binding


class PatterndetailsActivity5 : AppCompatActivity() {
    private lateinit var binding: ActivityPatterndetails5Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatterndetails5Binding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.backArrow.setOnClickListener{
            startActivity(Intent(this,PatternActivity::class.java))
        }
        binding.cancle.setOnClickListener{
            startActivity(Intent(this,PatternActivity::class.java))
        }
        binding.startt.setOnClickListener{
            startActivity(Intent(this,PatternOption5Draw::class.java))
        }


    }
}