package com.example.macetapp40.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Plant (

    @SerialName("Description")
    val description: String = "",

    @SerialName("Name")
    val name: String = "",

    @SerialName("Humidity")
    val humidity: Int? = 0,

    @SerialName("Watering")
    val watering: String? = "",

    @SerialName("PlantImage")
    val image: String = "",

    @SerialName("Date")
    val date: String? = "",

    @SerialName("PlantCode")
    val code: String = "",

    @SerialName("PlantTypeId")
    val typeId: Int = 0

)
