package com.example.LockerApp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.LockerApp.databinding.ActivityLockBinding


class LockActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLockBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backArrow.setOnClickListener {
            startActivity(Intent(this, WelcomeScreen4::class.java))
        }
        binding.btnPin.setOnClickListener {

            startActivity(Intent(this, PinStyle::class.java))
        }
        binding.btnFingerprint.setOnClickListener {
            startActivity(Intent(this, FaceUnlock::class.java))
        }
        binding.btnFaceUnlock.setOnClickListener {
            startActivity(Intent(this, FaceUnlock::class.java))
        }
        binding.btnPattern.setOnClickListener {
            startActivity(Intent(this, PatternActivity::class.java))
        }

    }


}