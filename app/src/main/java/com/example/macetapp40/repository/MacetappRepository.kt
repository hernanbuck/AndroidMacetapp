package com.example.macetapp40.repository

import com.example.macetapp40.model.Plant
import com.example.macetapp40.model.PlantType
import com.example.macetapp40.model.Post

interface MacetappRepository {
    suspend fun getPlantByUser(userId: String) : MacetappResult<Plant>
    suspend fun getPlantTypeId() : MacetappResult<PlantType>
    suspend fun setNewPlant(post: Post) : MacetappResult<Post>
    suspend fun setModifyPlant(post: Post) : MacetappResult<Post>
}