package com.frost.quizchallenge.utils

sealed class LoadState {
    object Loading: LoadState()
    object Success : LoadState()
    object Error : LoadState()
}