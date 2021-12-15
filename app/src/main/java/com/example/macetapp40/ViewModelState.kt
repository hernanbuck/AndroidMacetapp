package com.example.macetapp40

import android.net.Uri
import com.example.macetapp40.model.Plant
import com.example.macetapp40.model.PlantType
import com.example.macetapp40.model.Post

sealed class ViewModelState {
    object Loading : ViewModelState()
    data class UriPicSuccess(val uriPic: Uri?) : ViewModelState()
    data class PlantSuccess(val plant: Plant): ViewModelState()
    data class PlantTypeSuccess(val plantType: PlantType): ViewModelState()
    data class PostSuccess(val post: Post): ViewModelState()
    object Error : ViewModelState()
}
