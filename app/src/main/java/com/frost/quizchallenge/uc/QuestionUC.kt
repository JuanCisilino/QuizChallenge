package com.frost.quizchallenge.uc

import com.frost.quizchallenge.models.Country
import com.frost.quizchallenge.models.ResponseData
import com.frost.quizchallenge.repositories.CountryRepository
import javax.inject.Inject

class QuestionUC @Inject constructor(private val countryRepository: CountryRepository) {

    suspend fun getCountryList(): ResponseData {
        val result = countryRepository.getCountries()
        result.errorMessage?.let { return result }

        val list = refineList(result.countryList as ArrayList<Country>)

        return ResponseData(countryList = list)
    }

    private fun refineList(list: ArrayList<Country>): List<Country>{
        val listToRemove = arrayListOf<Country>()

        list.forEach { it.capital?:run { listToRemove.add(it) } }
        list.removeAll(listToRemove.toSet())
        return list.shuffled()
    }
}