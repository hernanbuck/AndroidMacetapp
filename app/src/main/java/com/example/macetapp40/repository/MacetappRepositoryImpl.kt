package com.example.macetapp40.repository

import com.example.macetapp40.model.Plant
import com.example.macetapp40.model.PlantType
import com.example.macetapp40.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MacetappRepositoryImpl(private val macetappAPI: MacetappAPI) : MacetappRepository {

    override suspend fun getPlantByUser(userId: String): MacetappResult<Plant> {
       return withContext(Dispatchers.IO) {
            executeRetrofitRequest { macetappAPI.getPlantByUserId(userId) }
        }
    }

    override suspend fun getPlantTypeId(): MacetappResult<PlantType> {
        return withContext(Dispatchers.IO) {
            executeRetrofitRequest { macetappAPI.getPlantTypeId() }
        }
    }

    override suspend fun setNewPlant(post: Post): MacetappResult<Post> {
        return withContext(Dispatchers.IO) {
            executeRetrofitRequest { macetappAPI.setNewPlant(post) }
        }
    }

    override suspend fun setModifyPlant(post: Post): MacetappResult<Post> {
        return withContext(Dispatchers.IO) {
            executeRetrofitRequest { macetappAPI.setModifyPlant(post) }
        }
    }

}
