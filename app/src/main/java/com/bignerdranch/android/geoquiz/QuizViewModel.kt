package com.bignerdranch.android.geoquiz

import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
    var currentIndex = 0
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    var currentQuestionAnswered: Boolean
        get() = questionBank[currentIndex].answered
        set(value) {
            questionBank[currentIndex].answered = value
        }

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    var userAnswer: Boolean?
        get() = questionBank[currentIndex].userAnswerCorrect
        set(value) {
            questionBank[currentIndex].userAnswerCorrect = value
        }

    val allQuestionsAnswered: Boolean
        get() = if (questionBank.lastIndex == currentIndex) questionBank.all { it.answered } else false

    fun calculatePercentOfCorrectAnswers(): Int = questionBank.filter {
        it.userAnswerCorrect == true
    }.size.times(100).div(questionBank.size)

}