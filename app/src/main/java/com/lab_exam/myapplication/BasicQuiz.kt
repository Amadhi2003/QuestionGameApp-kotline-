package com.lab_exam.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class BasicQuiz : AppCompatActivity() {

    private lateinit var quiztext: TextView
    private lateinit var aans: TextView
    private lateinit var bans: TextView
    private lateinit var cans: TextView
    private lateinit var dans: TextView
    private lateinit var funFactTextView: TextView

    private var questionItems = mutableListOf<QuestionsItems>()
    private var currentQuestions = 0
    private var correct = 0
    private var wrong = 0

    // Array of fun facts
    private val funFacts = arrayOf(
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

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        quiztext = findViewById(R.id.quizText)
        aans = findViewById(R.id.aanswer)
        bans = findViewById(R.id.banswer)
        cans = findViewById(R.id.canswer)
        dans = findViewById(R.id.danswer)
        funFactTextView = findViewById(R.id.funFactTextView) // Initialize funFactTextView

        // Display the high score
        displayHighScore()

        // Load questions
        loadAllQuestions()
        questionItems.shuffle()
        setQuestionScreen(currentQuestions)

        // Set click listeners for answer options
        aans.setOnClickListener {
            checkAnswer(questionItems[currentQuestions].answer1, aans)
        }
        bans.setOnClickListener {
            checkAnswer(questionItems[currentQuestions].answer2, bans)
        }
        cans.setOnClickListener {
            checkAnswer(questionItems[currentQuestions].answer3, cans)
        }
        dans.setOnClickListener {
            checkAnswer(questionItems[currentQuestions].answer4, dans)
        }

        // Set random fun fact
        setRandomFunFact()
    }

    private fun displayHighScore() {
        // Find the TextView for high score
        val highScoreTextView = findViewById<TextView>(R.id.highScoreTextView)

        // Retrieve the high score from SharedPreferences
        val sharedPreferences = getSharedPreferences("HighScores", Context.MODE_PRIVATE)
        val highScore = sharedPreferences.getInt("highScore", 0)

        // Display the high score
        highScoreTextView.text = "High Score: $highScore"
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
                // Set a new random fun fact
                setRandomFunFact()
            } else {
                val intent = Intent(this@BasicQuiz, ResultActivity::class.java)
                intent.putExtra("correct", correct)
                intent.putExtra("wrong", wrong)
                startActivity(intent)
                finish()
            }
        }, 500)
    }

    private fun setQuestionScreen(currentQuestions: Int) {
        val currentItem = questionItems[currentQuestions]
        quiztext.text = currentItem.questions
        aans.text = currentItem.answer1
        bans.text = currentItem.answer2
        cans.text = currentItem.answer3
        dans.text = currentItem.answer4
    }

    private fun loadAllQuestions() {
        questionItems = mutableListOf()
        val jsonQuiz = loadJsonFromAsset("easyquestions.json")
        try {
            val jsonObject = JSONObject(jsonQuiz)
            val questions = jsonObject.getJSONArray("easyquestions")
            for (i in 0 until questions.length()) {
                val question = questions.getJSONObject(i)
                val questionString = question.getString("question")
                val answer1String = question.getString("answer1")
                val answer2String = question.getString("answer2")
                val answer3String = question.getString("answer3")
                val answer4String = question.getString("answer4")
                val correctString = question.getString("correct")
                questionItems.add(QuestionsItems(
                    questionString,
                    answer1String,
                    answer2String,
                    answer3String,
                    answer4String,
                    correctString,

                ))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            // Handle JSON parsing error here, for example:
            // Toast.makeText(this, "Error loading questions", Toast.LENGTH_SHORT).show()
            // Log.e("BasicQuiz", "Error loading questions", e)
        }
    }


    private fun loadJsonFromAsset(s: String): String {
        var json = ""
        try {
            val inputStream: InputStream = assets.open(s)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, charset("UTF-8"))
        } catch (e: IOException) {
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

    private fun setRandomFunFact() {
        // Generate a random index to select a fun fact
        val randomIndex = (0 until funFacts.size).random()
        // Set the selected fun fact to the TextView
        funFactTextView.text = funFacts[randomIndex]
    }

    override fun onBackPressed() {
        super.onBackPressed() // Call the superclass implementation
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(this)
        materialAlertDialogBuilder.setTitle(R.string.app_name)
        materialAlertDialogBuilder.setMessage("Are you sure you want to exit the quiz?")
        materialAlertDialogBuilder.setNegativeButton(android.R.string.no) { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
        }
        materialAlertDialogBuilder.setPositiveButton(android.R.string.yes) { dialogInterface: DialogInterface, i: Int ->
            startActivity(Intent(this@BasicQuiz, MainActivity::class.java))
            finish()
        }
        materialAlertDialogBuilder.show()
    }

}
