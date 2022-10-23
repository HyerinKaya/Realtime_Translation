package com.jyh.realtimetranslation.presentation.offlinetranslation

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jyh.realtimetranslation.RealtimeTranslationApplication
import com.jyh.realtimetranslation.data.datastore.dataStore
import com.jyh.realtimetranslation.data.room.offline.OfflineMessageItem
import com.jyh.realtimetranslation.presentation.BaseViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class OfflineTranslationViewModel(
    private val context: Context
) : BaseViewModel() {
    val messageList = MutableLiveData(mutableListOf<OfflineMessageItem>())

    private var _myLangLiveData = MutableLiveData("ko-KO")
    val myLangLiveData = _myLangLiveData

    private var _otherLangLiveData = MutableLiveData("ko-KO")
    val otherLangLiveData = _otherLangLiveData

    private val langKey = stringPreferencesKey("lang")

    // 내 언어 정보 얻어오기
    override fun fetchData() = viewModelScope.launch {
        context.dataStore.data
            .map { preferences ->
                preferences[langKey]
            }.collect {
                it?.let { }
            }
    }

    fun addMessage(message: OfflineMessageItem){
        messageList.value?.add(message)
    }
}