package com.example.macetapp40.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlantType (

    @SerialName("PlantTypeID")
    val plantTypeID : Int = 0,
    @SerialName("Description")
    val description : String = ""

)
