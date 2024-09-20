package com.example.LockerApp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.LockerApp.databinding.ActivityPatternOption4DrawAgainBinding
import com.example.patternlock.PatternLockView

class PatternOption4DrawAgain : AppCompatActivity() {
    private lateinit var binding: ActivityPatternOption4DrawAgainBinding
    private lateinit var patternLockView: PatternLockView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPatternOption4DrawAgainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backArrow.setOnClickListener{
            startActivity(Intent(this@PatternOption4DrawAgain,PatternOption4Draw::class.java))
        }
        binding.textView8.setOnClickListener{
            startActivity(Intent(this@PatternOption4DrawAgain,PatternOption4Draw::class.java))
        }


       binding.patternLockViewDisplay.setOnPatternListener(object : PatternLockView.OnPatternListener {
            private var lastSelectedNode: Int? = null

            override fun onStarted() {}

            override fun onProgress(ids: ArrayList<Int>) {
                val currentNode = ids.lastOrNull()

                // Check if the current node is the same as the last selected node
                if (currentNode != null && currentNode == lastSelectedNode) {
                    // Remove the last node added to prevent selecting the same node consecutively
                    ids.removeAt(ids.size - 1)
                } else {
                    // Update last selected node
                    lastSelectedNode = currentNode
                }
            }

            override fun onComplete(ids: ArrayList<Int>): Boolean {
                // Remove consecutive duplicates from the entered pattern
                val modifiedEnteredPattern = removeConsecutiveDuplicates(ids)

                val sharedPreferences = getSharedPreferences("PatternPrefs", MODE_PRIVATE)
                val patternString = sharedPreferences.getString("savedPattern", "")
                val savedPattern =
                    patternString?.split(",")?.map { it.toInt() }?.let { ArrayList(it) }


                // Log the patterns for debugging
                println("Saved Pattern: $savedPattern")
                println("Entered Pattern: $modifiedEnteredPattern")

                return if (savedPattern == modifiedEnteredPattern) {
                    Toast.makeText(
                        this@PatternOption4DrawAgain,
                        "Pattern Correct!",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Proceed to the next activity if the pattern matches
                    startActivity(Intent(this@PatternOption4DrawAgain, AppLockFreeActivity::class.java))
                    true
                } else {
                    Toast.makeText(
                        this@PatternOption4DrawAgain,
                        "Pattern Incorrect!",
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }}


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

        })}}