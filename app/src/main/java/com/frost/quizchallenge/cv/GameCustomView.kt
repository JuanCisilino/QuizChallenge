package com.frost.quizchallenge.cv

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.frost.quizchallenge.R
import com.frost.quizchallenge.databinding.ViewGameBinding
import com.frost.quizchallenge.models.QuestionFormat

class GameCustomView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: ViewGameBinding =
        ViewGameBinding.inflate(LayoutInflater.from(context), this, true)
    private lateinit var context: Context
    private lateinit var answerList: List<String>
    private lateinit var correctAnswer: String
    private var correct = true

    var onRightClickCallback : (() -> Unit)? = null
    var onWrongClickCallback : (() -> Unit)? = null


    fun setComponents(context: Context, answer: String, answerList: List<String>, question: QuestionFormat) {
        this.context = context
        this.answerList = answerList
        correctAnswer = answer
        setComponent(question)
    }

    private fun setComponent(question: QuestionFormat) {
        setButtons()
        question.image?.let { glideIt(it) } ?:run { binding.flagImageView.visibility = View.GONE }
        with(binding){
            questionTextView.text = question.question
            answer1TextView.text = answerList[0]
            answer2TextView.text = answerList[1]
            answer3TextView.text = answerList[2]
            answer4TextView.text = answerList[3]
        }
    }

    private fun glideIt(url: String) {
        binding.flagImageView.visibility = View.VISIBLE
        Glide.with(context)
            .load(url)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.flagImageView)
    }

    private fun setButtons() {
        with(binding){
            answer1CardView.setOnClickListener {
                if (nextTextView.visibility == View.INVISIBLE) setBackground(answer1CardView, answer1TextView)
            }
            answer2CardView.setOnClickListener {
                if (nextTextView.visibility == View.INVISIBLE) setBackground(answer2CardView, answer2TextView)
            }
            answer3CardView.setOnClickListener {
                if (nextTextView.visibility == View.INVISIBLE) setBackground(answer3CardView, answer3TextView)
            }
            answer4CardView.setOnClickListener {
                if (nextTextView.visibility == View.INVISIBLE) setBackground(answer4CardView, answer4TextView)
            }
            nextTextView.setOnClickListener {
                allBackToWhite()
                checkIfCorrect()
            }
        }
    }

    private fun checkIfCorrect(){
        if (correct) {
            onRightClickCallback?.invoke()
        } else {
            correct = true
            onWrongClickCallback?.invoke()
        }
    }

    private fun setBackground(cardView: CardView, textView: TextView) {
        cardView.setBackgroundColor(validateBackgroundColor(textView.text.toString()))
    }

    private fun validateBackgroundColor(answer: String) =
        if (correctAnswer == answer) {
            showNext()
            context.getColor(R.color.right)
        } else {
            showNext()
            findOutCorrect()
            correct = false
            context.getColor(R.color.wrong)
        }

    private fun showNext() {
        binding.nextTextView.visibility = View.VISIBLE
    }

    private fun findOutCorrect() {
        when (answerList.indexOf(correctAnswer)) {
            0 -> binding.answer1CardView.setBackgroundColor(context.getColor(R.color.right))
            1 -> binding.answer2CardView.setBackgroundColor(context.getColor(R.color.right))
            2 -> binding.answer3CardView.setBackgroundColor(context.getColor(R.color.right))
            3 -> binding.answer4CardView.setBackgroundColor(context.getColor(R.color.right))
        }
    }

    fun allBackToWhite(){
        with(binding) {
            binding.answer1CardView.setBackgroundColor(context.getColor(R.color.white))
            binding.answer2CardView.setBackgroundColor(context.getColor(R.color.white))
            binding.answer3CardView.setBackgroundColor(context.getColor(R.color.white))
            binding.answer4CardView.setBackgroundColor(context.getColor(R.color.white))
            nextTextView.visibility = View.INVISIBLE
        }
    }
}