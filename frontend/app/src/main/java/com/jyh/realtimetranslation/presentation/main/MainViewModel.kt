package com.jyh.realtimetranslation.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jyh.realtimetranslation.presentation.BaseViewModel
import kotlinx.coroutines.launch

internal class MainViewModel(): BaseViewModel() {
    private var _loginStateMutableData = MutableLiveData<Boolean>(false)
    val loginStateLiveData = _loginStateMutableData

    override fun fetchData() = viewModelScope.launch {
    }
}