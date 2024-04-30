package com.lab_exam.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class ResultActivity : AppCompatActivity() {
    private lateinit var home: MaterialCardView
    private lateinit var correctt: TextView
    private lateinit var wrongt: TextView
    private lateinit var resultinfo: TextView
    private lateinit var resultscore: TextView
    private lateinit var resultImage: ImageView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        home = findViewById(R.id.returnHome)
        correctt = findViewById(R.id.correctScore)
        wrongt = findViewById(R.id.wrongScore)
        resultinfo = findViewById(R.id.resultInfo)
        resultscore = findViewById(R.id.resultScore)
        resultImage = findViewById(R.id.resultImage)

        sharedPreferences = getSharedPreferences("HighScores", Context.MODE_PRIVATE)

        val correct = intent.getIntExtra("correct", 0)
        val wrong = intent.getIntExtra("wrong", 0)
        val score = correct * 5

        correctt.text = correct.toString()
        wrongt.text = wrong.toString()
        resultscore.text = score.toString()

        val highScoreTextView = findViewById<TextView>(R.id.highScoreTextView)
        val highScore = sharedPreferences.getInt("highScore", 0)
        highScoreTextView.text = "High Score: $highScore"

        if (score > highScore) {
            with(sharedPreferences.edit()) {
                putInt("highScore", score)
                apply()
            }
        }

        when {
            correct >= 0 && correct <= 2 -> {
                resultinfo.text = "You have to take the test again"
                resultImage.setImageResource(R.drawable.ic_sad)
            }
            correct in 3..5 -> {
                resultinfo.text = "You have to try a little more"
                resultImage.setImageResource(R.drawable.ic_neutral)
            }
            correct in 6..8 -> {
                resultinfo.text = "You are pretty good"
                resultImage.setImageResource(R.drawable.ic_smile)
            }
            else -> {
                resultinfo.text = "You are very good congratulations"
                resultImage.setImageResource(R.drawable.ic_sad)
            }
        }

        home.setOnClickListener {
            startActivity(Intent(this@ResultActivity, MainActivity::class.java))
            finish()
        }
    }
}
