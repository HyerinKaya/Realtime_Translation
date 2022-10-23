package com.jyh.realtimetranslation.presentation.chatList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jyh.realtimetranslation.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class ChatListViewModel:BaseViewModel() {
    private var _isOnline = MutableLiveData<Boolean>(false)
    val isOnline = _isOnline

    override fun fetchData() = viewModelScope.launch {  }
}