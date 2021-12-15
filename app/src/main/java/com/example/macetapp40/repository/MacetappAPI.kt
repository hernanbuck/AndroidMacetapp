package com.example.macetapp40.repository

import com.example.macetapp40.model.Plant
import com.example.macetapp40.model.PlantType
import com.example.macetapp40.model.Post
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*

interface MacetappAPI {
    @GET("android/{userId}")
    suspend fun getPlantByUserId (@Path("userId") userId: String) : Response<Plant>

    @GET("plants")
    suspend fun getPlantTypeId() : Response<PlantType>

    @POST("plants")
    suspend fun setNewPlant (@Body post: Post) : Response<Post>

    @PATCH("plants")
    suspend fun setModifyPlant (@Body post: Post) : Response<Post>

    companion object {
        fun provideMacetappAPI(retrofit: Retrofit) : MacetappAPI {
            return retrofit.create(MacetappAPI::class.java)
        }
    }
}