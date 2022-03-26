package com.bignerdranch.android.geoquiz

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val KEY_ANSWERED = "QuestionAnswered"

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
        savedInstanceState.putBoolean(KEY_ANSWERED,quizViewModel.currentQuestionAnswered)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        val currentQuestionAnswered = savedInstanceState?.getBoolean(KEY_ANSWERED,false) ?: false
        quizViewModel.currentIndex = currentIndex
        quizViewModel.currentQuestionAnswered = currentQuestionAnswered

        Log.d(TAG, "got a quizViewModel $quizViewModel")
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            updateButtonsState()

            if (quizViewModel.allQuestionsAnswered) {
                calculateResults()
            }
        }
        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
            updateButtonsState()
            if (quizViewModel.allQuestionsAnswered) {
                calculateResults()
            }
        }
        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateButtonsState()
            updateQuestion()
        }
        questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateButtonsState()
            updateQuestion()
        }

        updateQuestion()
        updateButtonsState()
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

    private fun updateButtonsState() {
        val isQuestionAnswered = quizViewModel.currentQuestionAnswered
        trueButton.isEnabled = !isQuestionAnswered
        falseButton.isEnabled = !isQuestionAnswered
    }

    private fun calculateResults() {
        val percentOfCorrectAnswers = quizViewModel.calculatePercentOfCorrectAnswers()
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
        quizViewModel.currentQuestionAnswered = true
        val messageResId: Int
        if (userAnswer == correctAnswer) {
            quizViewModel.userAnswer = true
            messageResId = R.string.correct_toast
        } else {
            quizViewModel.userAnswer = false
            messageResId = R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()
    }
}