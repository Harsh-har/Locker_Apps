package com.example.LockerApp


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.LockerApp.databinding.ActivityPremimumBinding
class PremimumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPremimumBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPremimumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button3.setOnClickListener({
            startActivity(Intent(this,LockMainActivity::class.java))
        })

    }
}