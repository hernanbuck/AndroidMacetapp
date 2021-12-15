package com.example.macetapp40

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.macetapp40.di.viewModelModule
import com.example.macetapp40.model.Post
import com.example.macetapp40.repository.MacetappRepository
import com.example.macetapp40.repository.MacetappResult
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class ShareDataViewModel(
    private val macetappRepository: MacetappRepository,
    private val _viewModelState: MutableLiveData<ViewModelState>,
) : ViewModel() {

    val getViewModelState: LiveData<ViewModelState>
    get() = _viewModelState
    private var uriPic: Uri? = null


    //Get plant//
    fun getPlantByUserId(userId: String) {
        viewModelScope.launch {
            _viewModelState.value = when (val result = macetappRepository.getPlantByUser(userId)) {
                MacetappResult.Loading -> ViewModelState.Loading
                is MacetappResult.Error -> ViewModelState.Error
                is MacetappResult.Success -> ViewModelState.PlantSuccess(result.data)
            }
        }
    }

    //Get plant Id//
    fun getPlantTypeId() {
        viewModelScope.launch {
            _viewModelState.value = when (val result = macetappRepository.getPlantTypeId()) {
                MacetappResult.Loading -> ViewModelState.Loading
                is MacetappResult.Error -> ViewModelState.Error
                is MacetappResult.Success -> ViewModelState.PlantTypeSuccess(result.data)
            }
        }
    }

    //Add new plant//
    fun setNewPlant(post: Post) {
        viewModelScope.launch {
            _viewModelState.value = when (val result = macetappRepository.setNewPlant(post)) {
                MacetappResult.Loading -> ViewModelState.Loading
                is MacetappResult.Error -> ViewModelState.Error
                is MacetappResult.Success -> ViewModelState.PostSuccess(result.data)
            }
        }
    }

    //Modify plant
    fun setModifyPlant(post: Post) {
        viewModelScope.launch {
            _viewModelState.value = when (val result = macetappRepository.setModifyPlant(post)) {
                MacetappResult.Loading -> ViewModelState.Loading
                is MacetappResult.Error -> ViewModelState.Error
                is MacetappResult.Success -> ViewModelState.PostSuccess(result.data)
            }
        }
    }

    //UriPhoto//
    fun setUriPhoto (uriPhoto : Uri?) {
        uriPic = uriPhoto
    }

    fun getUriPhoto () {
        viewModelScope.launch {
            getUriPic().onStart {
                _viewModelState.value = ViewModelState.Loading
            }
            _viewModelState.value = ViewModelState.UriPicSuccess(uriPic)
        }
    }

    private fun getUriPic() = flow <Uri>{ uriPic }

}