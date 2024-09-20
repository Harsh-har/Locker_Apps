package com.example.LockerApp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.LockerApp.databinding.ActivityPatternBinding



class PatternActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatternBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPatternBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pattern.setOnClickListener {
            startActivity(Intent(this, PatterndetailsActivity::class.java))
        }
        binding.backArrow.setOnClickListener {
            startActivity(Intent(this, LockActivity::class.java))
        }
        binding.btn.setOnClickListener{
            startActivity(Intent(this, PatterndetailsActivity4::class.java))
        }
        binding.btnFaceUnlock.setOnClickListener{
            startActivity(Intent(this, PatterndetailsActivity5::class.java))

        }

    }
}
