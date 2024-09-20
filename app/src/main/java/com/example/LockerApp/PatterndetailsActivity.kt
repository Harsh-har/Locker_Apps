package com.example.LockerApp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import com.example.LockerApp.databinding.ActivityPatterndetailsBinding

class PatterndetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatterndetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatterndetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backArrow.setOnClickListener{
            startActivity(Intent(this,PatternActivity::class.java))
        }
        binding.cancle.setOnClickListener{
            startActivity(Intent(this,PatternActivity::class.java))
        }
        binding.start.setOnClickListener{
            startActivity(Intent(this,PatternOption3Draw::class.java))
        }


    }
}
