package com.lab_exam.myapplication


import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.json.JSONObject
import android.content.Context

import java.io.InputStream

class DifficultQuiz : AppCompatActivity() {

    private lateinit var quiztext: TextView
    private lateinit var aans: TextView
    private lateinit var bans: TextView
    private lateinit var cans: TextView
    private lateinit var dans: TextView
    private lateinit var highScoreTextView: TextView // Added
    private lateinit var funFactTextView: TextView // Added

    private lateinit var questionItems: MutableList<QuestionsItems>
    private var currentQuestions = 0
    private var correct = 0
    private var wrong = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        quiztext = findViewById(R.id.quizText)
        aans = findViewById(R.id.aanswer)
        bans = findViewById(R.id.banswer)
        cans = findViewById(R.id.canswer)
        dans = findViewById(R.id.danswer)
        highScoreTextView = findViewById(R.id.highScoreTextView) // Added
        funFactTextView = findViewById(R.id.funFactTextView) // Added

        loadAllQuestions()
        questionItems.shuffle()
        setQuestionScreen(currentQuestions)

        aans.setOnClickListener {
            checkAnswer(aans.text.toString(), aans)
        }

        bans.setOnClickListener {
            checkAnswer(bans.text.toString(), bans)
        }

        cans.setOnClickListener {
            checkAnswer(cans.text.toString(), cans)
        }

        dans.setOnClickListener {
            checkAnswer(dans.text.toString(), dans)
        }

        // Display the high score
        displayHighScore()
        // Display a random fun fact
        setRandomFunFact()
    }

    private fun setQuestionScreen(currentQuestions: Int) {
        quiztext.text = questionItems[currentQuestions].questions
        aans.text = questionItems[currentQuestions].answer1
        bans.text = questionItems[currentQuestions].answer2
        cans.text = questionItems[currentQuestions].answer3
        dans.text = questionItems[currentQuestions].answer4
    }

    private fun checkAnswer(selectedAnswer: String, selectedView: TextView) {
        val correctAnswer = questionItems[currentQuestions].correct

        if (selectedAnswer == correctAnswer) {
            correct++
            // Change the text color of the selected answer to green
            selectedView.setTextColor(Color.GREEN)
        } else {
            wrong++
            // Change the text color of the selected answer to red
            selectedView.setTextColor(Color.RED)
        }

        // Disable click listeners on all answers
        aans.isClickable = false
        bans.isClickable = false
        cans.isClickable = false
        dans.isClickable = false

        // Delay for 500 milliseconds before moving to the next question
        Handler().postDelayed({
            currentQuestions++
            if (currentQuestions < questionItems.size) {
                setQuestionScreen(currentQuestions)
                // Reset text color for the next question
                resetAnswerColors()
                // Enable click listeners on all answers for the next question
                aans.isClickable = true
                bans.isClickable = true
                cans.isClickable = true
                dans.isClickable = true
                // Display a new random fun fact
                setRandomFunFact()
            } else {
                val intent = Intent(this@DifficultQuiz, ResultActivity::class.java)
                intent.putExtra("correct", correct)
                intent.putExtra("wrong", wrong)
                startActivity(intent)
                finish()
            }
        }, 500)
    }

    private fun loadAllQuestions() {
        questionItems = mutableListOf()
        val jsonQuiz = loadJsonFromAsset("difficultquestions.json")
        try {
            val jsonObject = JSONObject(jsonQuiz)
            val questions = jsonObject.getJSONArray("difficultquestions")
            for (i in 0 until questions.length()) {
                val question = questions.getJSONObject(i)
                val questionString = question.getString("question")
                val answer1String = question.getString("answer1")
                val answer2String = question.getString("answer2")
                val answer3String = question.getString("answer3")
                val answer4String = question.getString("answer4")
                val correctString = question.getString("correct")

                questionItems.add(QuestionsItems(questionString, answer1String, answer2String, answer3String, answer4String, correctString))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadJsonFromAsset(s: String): String {
        var json = ""
        try {
            val inputStream = assets.open(s)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, charset("UTF-8"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return json
    }

    private fun resetAnswerColors() {
        aans.setTextColor(Color.BLACK)
        bans.setTextColor(Color.BLACK)
        cans.setTextColor(Color.BLACK)
        dans.setTextColor(Color.BLACK)
    }

    private fun displayHighScore() {
        // Retrieve the high score from SharedPreferences
        val sharedPreferences = getSharedPreferences("HighScores", Context.MODE_PRIVATE)
        val highScore = sharedPreferences.getInt("highScore", 0)

        // Display the high score
        highScoreTextView.text = "High Score: $highScore"
    }

    private fun setRandomFunFact() {
        // Array of fun facts
        val funFacts = arrayOf(
            "Fun Fact : Rats killed over 75 million Europeans in the Middle Ages.",
            "Fun Fact : The shortest war in history was between Britain and Zanzibar on August 27, 1896. Zanzibar surrendered after 38 minutes.",
            "Fun Fact : The Eiffel Tower can be 15 cm taller during the summer due to thermal expansion of the iron.",
            "Fun Fact : Queen Elizabeth II was never supposed to be queen.",
            "Fun Fact : The Leaning Tower of Pisa never stood up straight.",
            "Fun Fact : The tiny Easter Island is home to 887 giant head statues.",
            "Fun Fact : The first living creature in space was a dog named Laika.",
            "Fun Fact : In Ancient Egypt, the New Year celebration was called Wepet Renpet.",
            "Fun Fact : The Vikings loved grooming.",
        )

        // Generate a random index to select a fun fact
        val randomIndex = (0 until funFacts.size).random()
        // Set the selected fun fact to the TextView
        funFactTextView.text = funFacts[randomIndex]
    }

    override fun onBackPressed() {
        super.onBackPressed() // Call the superclass method
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(this@DifficultQuiz)
        materialAlertDialogBuilder.setTitle(R.string.app_name)
        materialAlertDialogBuilder.setMessage("Are you sure you want to exit the quiz?")
        materialAlertDialogBuilder.setNegativeButton(android.R.string.no) { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
        }
        materialAlertDialogBuilder.setPositiveButton(android.R.string.yes) { dialogInterface: DialogInterface, i: Int ->
            startActivity(Intent(this@DifficultQuiz, MainActivity::class.java))
            finish()
        }
        materialAlertDialogBuilder.show()
    }

}