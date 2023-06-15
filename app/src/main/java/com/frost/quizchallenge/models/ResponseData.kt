package com.frost.quizchallenge.models

data class ResponseData(
    val errorMessage: String?=null,
    val countryList: List<Country>?= listOf()
)
