package com.heliushouse.starwars.repository

import com.heliushouse.starwars.api.StarWarsApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StarWarsRepository @Inject constructor(private val apiService: StarWarsApi) {

}