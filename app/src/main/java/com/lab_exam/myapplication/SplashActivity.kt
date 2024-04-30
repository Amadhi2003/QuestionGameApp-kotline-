package com.lab_exam.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SplashActivity : AppCompatActivity() {

    companion object {
        var checked: Int = 0
        const val MyPREFERENCES = "QuizPrefs"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val sharedPreferences: SharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        val default1: Int = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            AppCompatDelegate.MODE_NIGHT_NO -> 0
            AppCompatDelegate.MODE_NIGHT_YES -> 1
            else -> 2
        }
        checked = sharedPreferences.getInt("checked", default1)
        when (checked) {
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            3 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
        }

        val mSplashThread = Thread {
            try {
                Thread.sleep(2000)
            } catch (ignored: InterruptedException) {
            } finally {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        }

        mSplashThread.start()
    }
}
