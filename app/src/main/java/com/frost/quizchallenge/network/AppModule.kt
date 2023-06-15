package com.frost.quizchallenge.network

import com.frost.quizchallenge.repositories.CountryRepository
import com.frost.quizchallenge.uc.QuestionUC
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit() = Retrofit.Builder()
        .baseUrl("https://restcountries.com/v3.1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CountryApi::class.java)

    @Singleton
    @Provides
    fun providesApiToRepository(countryApi: CountryApi) = CountryRepository(countryApi)

    @Singleton
    @Provides
    fun providesRepositoryToQuestionUC(countryRepository: CountryRepository) = QuestionUC(countryRepository)
}