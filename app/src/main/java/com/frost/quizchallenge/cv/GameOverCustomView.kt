package com.frost.quizchallenge.cv

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.frost.quizchallenge.R
import com.frost.quizchallenge.databinding.ViewGameOverBinding

class GameOverCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: ViewGameOverBinding =
        ViewGameOverBinding.inflate(LayoutInflater.from(context), this, true)

    var onAgainClickCallback : (() -> Unit)? = null

    init {
        binding.againTextView.setOnClickListener { onAgainClickCallback?.invoke() }
    }

    fun setCorrectAnswer(context: Context, number: Int) {
        binding.resultTextView.text =
            if (number == 1) context.getString(R.string.result1, number)
            else context.getString(R.string.result, number)
    }

}