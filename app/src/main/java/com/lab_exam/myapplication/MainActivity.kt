package com.lab_exam.myapplication

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    private lateinit var easycard: MaterialCardView
    private lateinit var difficultcard: MaterialCardView
    private lateinit var aboutcard: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences: SharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE)

        easycard = findViewById(R.id.easyCard)
        difficultcard = findViewById(R.id.difficultCard)
        aboutcard = findViewById(R.id.aboutCard)

        easycard.setOnClickListener {
            startActivity(Intent(this@MainActivity, BasicQuiz::class.java))
            finish()
        }
        difficultcard.setOnClickListener {
            startActivity(Intent(this@MainActivity, DifficultQuiz::class.java))
            finish()
        }
        aboutcard.setOnClickListener {
            startActivity(Intent(this@MainActivity, AboutActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(this)
        materialAlertDialogBuilder.setTitle(R.string.app_name)
        materialAlertDialogBuilder.setMessage("Are you sure you want to exit the app?")
        materialAlertDialogBuilder.setNegativeButton(android.R.string.no) { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
        }
        materialAlertDialogBuilder.setPositiveButton(android.R.string.yes) { dialogInterface: DialogInterface, i: Int ->
            finish()
        }
        materialAlertDialogBuilder.show()
    }

    companion object {
        const val MyPREFERENCES = "QuizPrefs"
    }
}
