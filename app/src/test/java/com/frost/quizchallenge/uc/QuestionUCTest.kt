package com.frost.quizchallenge.uc

import com.frost.quizchallenge.models.ResponseData
import com.frost.quizchallenge.repositories.CountryRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class QuestionUCTest{

    @RelaxedMockK
    private lateinit var repository: CountryRepository

    lateinit var questionUC: QuestionUC

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        questionUC = QuestionUC(repository)
    }

    @Test
    fun `when the api doesnt return anything then return errorMessage different to null`() = runBlocking{
        //Given
        coEvery { repository.getCountries() } returns ResponseData(errorMessage = "error")

        //When
        val response = questionUC.getCountryList()

        //Then
        assert(response.errorMessage != null)

    }

    @Test
    fun `when the api returns something then return empty list`() = runBlocking{
        //Given
        coEvery { repository.getCountries() } returns ResponseData(countryList = arrayListOf())

        //When
        val response = questionUC.getCountryList()

        //Then
        assert(response.countryList != null)

    }
}