package com.heliushouse.starwars.api

import com.heliushouse.starwars.model.PeopleResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface StarWarsApi {

    @GET("people")
    fun getPeople(): Flow<PeopleResponse>

    @GET("people")
    fun search(@Query("search") query: String): Flow<PeopleResponse>

    companion object {
        const val BASE_API = "https://swapi.dev/api/"
    }
}