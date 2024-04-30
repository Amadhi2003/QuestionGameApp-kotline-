package com.lab_exam.myapplication

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class AboutActivity : AppCompatActivity() {
    private lateinit var developerCard: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        developerCard = findViewById(R.id.developerCard)

        // Your existing code for developerCard click listener
        developerCard.setOnClickListener {
            val uri = Uri.parse("https://www.instagram.com/developer")
            val likeIntent = Intent(Intent.ACTION_VIEW, uri)
            likeIntent.setPackage("com.instagram.android")
            try {
                startActivity(likeIntent)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/developer")))
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
