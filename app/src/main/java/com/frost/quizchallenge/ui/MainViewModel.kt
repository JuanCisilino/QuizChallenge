package com.frost.quizchallenge.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frost.quizchallenge.models.Country
import com.frost.quizchallenge.utils.LoadState
import com.frost.quizchallenge.models.QuestionFormat
import com.frost.quizchallenge.uc.QuestionUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val questionUC: QuestionUC): ViewModel() {

    var loadStateLiveData = MutableLiveData<LoadState>()
    var errorLiveData = MutableLiveData<String>()
    var questionLiveData = MutableLiveData<QuestionFormat>()

    var countryList = arrayListOf<Country>()
        private set
    var correctAnswer = ""
        private set
    var answerList = arrayListOf<String>()
        private set

    var correctAnswers = 0

    private var firstTime = true

    fun start(){
        loadStateLiveData.postValue(LoadState.Loading)
        viewModelScope.launch {
            val result = questionUC.getCountryList()
            countryList.clear()
            result.errorMessage?.let { errorLiveData.postValue(it) }
            result.countryList
                ?.let {
                    countryList = it as ArrayList<Country>
                    createQuestion()
                    firstTime = false
                }
                ?:run { loadStateLiveData.postValue(LoadState.Error) }
        }
    }

    fun createQuestion() {
        if (!firstTime) loadStateLiveData.postValue(LoadState.Loading)
        answerList.clear()
        correctAnswer = ""
        val selectedCountryToGenerateQuestion = getRandomCountry()
        if (esPar(countryList.indexOf(selectedCountryToGenerateQuestion))) {
            generateFlagQuestion(selectedCountryToGenerateQuestion)
        } else {
            generateCapitalQuestion(selectedCountryToGenerateQuestion)
        }
    }

    private fun generateCapitalQuestion(selectedCountry: Country) {
        val question = QuestionFormat(
            question = "¿${selectedCountry.capital} es la capital de que pais?",
            image = null
        )
        correctAnswer = selectedCountry.name
        generateCountryAnswer(selectedCountry)
        questionLiveData.postValue(question)
        loadStateLiveData.postValue(LoadState.Success)
    }

    private fun generateFlagQuestion(selectedCountry: Country) {
        val question = QuestionFormat(
            question = "¿De que pais es esta bandera?",
            image = selectedCountry.flag
        )
        correctAnswer = selectedCountry.name
        generateCountryAnswer(selectedCountry)
        questionLiveData.postValue(question)
        loadStateLiveData.postValue(LoadState.Success)
    }

    private fun generateCountryAnswer(selectedCountry: Country) {
        val randomCountry1 = getRandomCountry()
        val randomCountry2 = getRandomCountry()
        val randomCountry3 = getRandomCountry()
        if (randomCountry1 != selectedCountry &&
            randomCountry1 != randomCountry2 &&
            randomCountry2 != selectedCountry &&
            randomCountry3 != selectedCountry &&
            randomCountry3 != randomCountry2 &&
            randomCountry3 != randomCountry1) {
            answerList.add(selectedCountry.name)
            answerList.add(randomCountry1.name)
            answerList.add(randomCountry2.name)
            answerList.add(randomCountry3.name)
            answerList.shuffle()
            correctAnswer = selectedCountry.name
        } else {
            generateCountryAnswer(selectedCountry)
        }
    }

    private fun getRandomCountry() = countryList.random()

    private fun esPar(index: Int) = index % 2 == 0

    fun isValidAnswer(answer: String) = correctAnswer == answer

}