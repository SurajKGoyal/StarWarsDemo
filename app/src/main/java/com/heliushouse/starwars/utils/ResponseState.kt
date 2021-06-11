package com.heliushouse.starwars.utils

import com.heliushouse.starwars.model.PeopleResponse

sealed class ResponseState {
    data class Success(val response: PeopleResponse): ResponseState()
    object Loading : ResponseState()
    data class Error(val msg: String): ResponseState()

}
