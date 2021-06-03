package com.heliushouse.starwars.utils

import com.heliushouse.starwars.model.PeopleResponse

sealed class ResponseState {
    data class Success(val response: PeopleResponse): ResponseState()
    data class Loading(val msg: String): ResponseState()
    data class Error(val msg: String): ResponseState()

}
