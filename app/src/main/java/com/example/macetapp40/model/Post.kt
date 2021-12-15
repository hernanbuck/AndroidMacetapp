package com.example.macetapp40.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Post(

    @SerialName("plantCode")
    val plantCode: String? = "" ,

    @SerialName("plantName")
    val planName: String? = "" ,

    @SerialName("plantTypeId")
    val plantTypeId: Int? = 0 ,

    @SerialName("userId")
    val userId: String? = "" ,

    @SerialName("plantImage")
    val plantImage: String? = ""
               )