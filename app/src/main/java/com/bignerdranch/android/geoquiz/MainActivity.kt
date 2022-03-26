package com.bignerdranch.android.geoquiz

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    private lateinit var nextButton: Button

    private lateinit var questionTextView: TextView

    private val quizViewModel: QuizViewModel by viewModels()

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        Log.d(TAG, "got a quizViewModel $quizViewModel")
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
            quizViewModel.moveToNext()
            isAnswered(currentIndex)
            updateQuestion()
        }
        questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            isAnswered(currentIndex)
            updateQuestion()
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

    private fun isAnswered(index: Int) {
        val isQuestionAnswered = quizViewModel.isQuestionAnswered
        trueButton.isEnabled = !isQuestionAnswered
        falseButton.isEnabled = !isQuestionAnswered
    }

    private fun isAllQuestionsAnswered(): Boolean {
        return if (questionBank.lastIndex == currentIndex) {
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
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId: Int
        quizViewModel.currentQuestionAnswered = true
        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            quizViewModel.isQuestionAnswered = true
        } else {
            messageResId = R.string.incorrect_toast
            quizViewModel.isQuestionAnswered = false
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()
    }
}