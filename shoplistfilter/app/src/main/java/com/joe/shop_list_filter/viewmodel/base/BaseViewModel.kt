package com.joe.shop_list_filter.viewmodel.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joe.shop_list_filter.model.Repository
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()
    private val _progress = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<String>()
    val progress: LiveData<Boolean> = _progress
    val error: LiveData<String> = _error

    fun setProgress(isLoading: Boolean) {
        _progress.value = isLoading
    }

    fun error(message: String) {
        setProgress(false)
        _error.value = message
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}