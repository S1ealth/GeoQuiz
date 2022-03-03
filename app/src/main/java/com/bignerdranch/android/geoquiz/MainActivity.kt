package com.bignerdranch.android.geoquiz

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    private lateinit var nextButton: Button

    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            isAnswered(currentIndex)
            if (isAllQuestionsAnswered()) {
                calculateResults()
            }
        }
        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
            isAnswered(currentIndex)
            if (isAllQuestionsAnswered()) {
                calculateResults()
            }
        }
        nextButton.setOnClickListener {
            moveToNextQuestion()
        }
        questionTextView.setOnClickListener {
            moveToNextQuestion()
        }

        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun moveToNextQuestion() {
        currentIndex = (currentIndex + 1) % questionBank.size
        isAnswered(currentIndex)
        updateQuestion()
    }

    private fun isAnswered(index: Int) {
        val isQuestionAnswered = questionBank[index].answered
        trueButton.isEnabled = !isQuestionAnswered
        falseButton.isEnabled = !isQuestionAnswered
    }

    private fun isAllQuestionsAnswered(): Boolean {
        return if(questionBank.lastIndex == currentIndex) {
            questionBank.all { it.answered }
        } else {
            false
        }
    }

    private fun calculateResults() {
        val percentOfCorrectAnswers: Int = questionBank.filter {
            it.isAnswerCorrect == true
        }.size
            .times(100)
            .div(questionBank.size)
        Toast.makeText(
            this,
            "you answered correctly on $percentOfCorrectAnswers% questions",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId: Int
        questionBank[currentIndex].answered = true
        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            questionBank[currentIndex].isAnswerCorrect = true
        } else {
            messageResId = R.string.incorrect_toast
            questionBank[currentIndex].isAnswerCorrect = false
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()
    }
}