package com.frost.quizchallenge.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.frost.quizchallenge.*
import com.frost.quizchallenge.databinding.ActivityMainBinding
import com.frost.quizchallenge.utils.LoadState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSwipe()
        checkForInternetAndStart()
        subscribeToLiveData()
        setCallbacks()
    }

    private fun setSwipe() {
        binding.refreshLayout.setOnRefreshListener {
            if (binding.gameCV.visibility == View.GONE) checkForInternetAndStart()
        }
    }

    private fun setCallbacks() {
        binding.gameCV.onWrongClickCallback = { showGameOver() }
        binding.gameCV.onRightClickCallback = {
            viewModel.correctAnswers++
            viewModel.createQuestion()
        }
    }

    private fun checkForInternetAndStart() {
        if (isInternetAvailable()) {
            viewModel.start()
        } else {
            showShimmer()
            showToast(getString(R.string.no_internet))
        }
        binding.refreshLayout.isRefreshing = false
    }

    private fun subscribeToLiveData() {
        viewModel.loadStateLiveData.observe(this) { handleResponse(it) }
        viewModel.errorLiveData.observe(this) { showToast(it) }
        viewModel.questionLiveData.observe(this) {
            binding.gameCV.setComponents(
                this,
                viewModel.correctAnswer,
                viewModel.answerList,
                it)
        }
    }

    private fun handleResponse(loadState: LoadState) {
        when(loadState){
            LoadState.Loading -> showShimmer()
            LoadState.Success -> hideShimmer()
            else -> showToast(getString(R.string.error))
        }
    }

    private fun hideShimmer() {
        with(binding) {
            loadingCV.visibility = View.GONE
            gameCV.visibility = View.VISIBLE
        }
    }

    private fun showShimmer() {
        with(binding) {
            loadingCV.visibility = View.VISIBLE
            gameCV.visibility = View.GONE
        }
    }

    private fun showGameOver() {
        with(binding){
            gameCV.visibility = View.GONE
            gameOverCV.visibility = View.VISIBLE
            gameOverCV.setCorrectAnswer(this@MainActivity, viewModel.correctAnswers)
            viewModel.correctAnswers = 0
            gameOverCV.onAgainClickCallback = {
                gameOverCV.visibility = View.GONE
                viewModel.createQuestion()
            }
        }
    }


}