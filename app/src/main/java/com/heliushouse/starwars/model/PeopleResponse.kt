package com.heliushouse.starwars.model

import com.google.gson.annotations.SerializedName

data class PeopleResponse(
    @SerializedName("results")
    val peoples: List<People>
)
