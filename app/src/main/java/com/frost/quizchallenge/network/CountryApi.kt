package com.frost.quizchallenge.network

import com.frost.quizchallenge.models.CountryBack
import retrofit2.Response
import retrofit2.http.GET

interface CountryApi {

    @GET("all/")
    suspend fun getCountries(): Response<List<CountryBack>>
}