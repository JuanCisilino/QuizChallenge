package com.frost.quizchallenge.repositories

import com.frost.quizchallenge.models.ResponseData
import com.frost.quizchallenge.network.CountryApi
import javax.inject.Inject

class CountryRepository @Inject constructor(private val countryApi: CountryApi) {

    suspend fun getCountries(): ResponseData {
        val response = countryApi.getCountries()
        response.errorBody()?.let { return ResponseData(errorMessage = it.toString()) }
        val list = response.body()
        return ResponseData(countryList = list?.map { it.mapToCountry() })
    }

}