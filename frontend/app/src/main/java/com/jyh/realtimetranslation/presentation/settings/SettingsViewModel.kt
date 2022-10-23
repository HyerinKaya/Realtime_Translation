package com.jyh.realtimetranslation.presentation.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jyh.realtimetranslation.data.datastore.dataStore
import com.jyh.realtimetranslation.presentation.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class SettingsViewModel(
    private val context: Context
):BaseViewModel() {
    private var _loginStateMutableData = MutableLiveData<Boolean>(false)
    val loginStateLiveData = _loginStateMutableData

    private val loginKey = booleanPreferencesKey("isLogin")
    private val tokenKey = stringPreferencesKey("token")
    private val token: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[tokenKey]
        }

    override fun fetchData() = viewModelScope.launch {
        token.collect{
            _loginStateMutableData.postValue(it != null)
        }
    }


    fun signOut(){
        _loginStateMutableData.postValue(false)
        viewModelScope.launch { logOut() }
    }

    private suspend fun logOut() {
        context.dataStore.edit { settings ->
            settings[loginKey] = false
            settings.remove(tokenKey)
        }
    }
}