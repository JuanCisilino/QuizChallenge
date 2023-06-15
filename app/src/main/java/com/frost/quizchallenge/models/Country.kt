package com.frost.quizchallenge.models

data class CountryBack(
    val name: Name,
    val capital: List<String>?,
    val flags: Flag
) {

    fun mapToCountry() = Country(
        name = this.name.common,
        capital = this.capital?.joinToString(" "),
        flag = flags.png
    )

}

data class Name(
    val common: String
)

data class Flag(
    val png: String
)

data class Country(
    val name: String,
    val capital: String?,
    val flag: String
)