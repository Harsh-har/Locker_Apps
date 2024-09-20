package com.example.LockerApp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.LockerApp.databinding.ActivityAppLockFreeBinding


class AppLockFreeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppLockFreeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      binding= ActivityAppLockFreeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button2.setOnClickListener({
            startActivity(Intent(this,PremimumActivity::class.java))
        })

    }
}