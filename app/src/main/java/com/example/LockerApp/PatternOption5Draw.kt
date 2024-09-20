package com.example.LockerApp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.LockerApp.databinding.ActivityPatternOption5DrawBinding
import com.example.patternlock.PatternLockView
class PatternOption5Draw : AppCompatActivity() {
    private lateinit var patternLockView: PatternLockView
    private lateinit var binding: ActivityPatternOption5DrawBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPatternOption5DrawBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backArrow.setOnClickListener{
            startActivity(Intent(this@PatternOption5Draw, PatterndetailsActivity5::class.java))
        }
        binding.textView8.setOnClickListener{
            startActivity(Intent(this@PatternOption5Draw, PinStyle::class.java))
        }
         binding.patternLockViewDisplay.setOnPatternListener(object : PatternLockView.OnPatternListener {
            override fun onStarted() {}

            override fun onProgress(ids: ArrayList<Int>) {}

            override fun onComplete(ids: ArrayList<Int>): Boolean {
                if (ids.size > 2) {
                    savePattern(ids)
                    startActivity(Intent(this@PatternOption5Draw, PatternOption5DrawAgain::class.java))
                    return true
                }
                return false
            }
        })
    }

    private fun savePattern(pattern: ArrayList<Int>) {
        // Remove consecutive duplicates from the pattern
        val modifiedPattern = removeConsecutiveDuplicates(pattern)

        val sharedPreferences = getSharedPreferences("PatternPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert the modified pattern list to a comma-separated string
        val patternString = modifiedPattern.joinToString(separator = ",")

        // Save the pattern string in SharedPreferences
        editor.putString("savedPattern", patternString)
        editor.apply()
    }

    private fun removeConsecutiveDuplicates(ids: ArrayList<Int>): ArrayList<Int> {
        if (ids.isEmpty()) return ids

        val result = ArrayList<Int>()
        result.add(ids[0])

        for (i in 1 until ids.size) {
            if (ids[i] != ids[i - 1]) {
                result.add(ids[i])
            }
        }
        return result
    }
}
