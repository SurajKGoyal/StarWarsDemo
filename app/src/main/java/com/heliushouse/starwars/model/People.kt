package com.heliushouse.starwars.model

import com.google.gson.annotations.SerializedName

data class People(
    @SerializedName("name")
    val name: String
)
