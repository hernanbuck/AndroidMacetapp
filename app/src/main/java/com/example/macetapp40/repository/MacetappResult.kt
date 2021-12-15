package com.example.macetapp40.repository

sealed class MacetappResult<out T : Any> {
    data class Success <out T : Any> (val data: T) : MacetappResult<T>()
    data class Error (val exception: String?) : MacetappResult<Nothing>()
    object Loading : MacetappResult<Nothing>()
}
