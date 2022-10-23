package com.jyh.realtimetranslation.presentation.people

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.jyh.realtimetranslation.data.datastore.dataStore
import com.jyh.realtimetranslation.domain.GetPeopleUseCase
import com.jyh.realtimetranslation.presentation.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class PeopleViewModel(
    private val getPeopleUseCase: GetPeopleUseCase,
    private val context: Context
) : BaseViewModel() {

    private var _peopleStateLiveData = MutableLiveData<PeopleState>(PeopleState.UnInitialized)
    val peopleStateLiveData: LiveData<PeopleState> get() = _peopleStateLiveData

    private var _loginStateLiveData = MutableLiveData<LoginState>(LoginState.Uninitialized)
    val loginStateLiveData: LiveData<LoginState> = _loginStateLiveData

    private val loginKey = booleanPreferencesKey("isLogin")
    private val isLogin: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[loginKey] ?: false
        }

    private val tokenKey = stringPreferencesKey("token")
    private val token: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[tokenKey]
        }

    override fun fetchData() = viewModelScope.launch {
        setState(PeopleState.Loading)
        setState(PeopleState.Success(getPeopleUseCase()))

        setLoginState(LoginState.Loading)

        token.collect {
            it?.let{
                setLoginState(
                    LoginState.Login(it)
                )
                logIn()
            } ?: kotlin.run{
                setLoginState(
                    LoginState.Success.NotRegistered
                )
            }
        }
    }

    private fun setLoginState(loginState: LoginState) {
        _loginStateLiveData.postValue(loginState)
    }

    private fun setState(state: PeopleState) {
        _peopleStateLiveData.postValue(state)
    }

    private suspend fun logIn() {
        context.dataStore.edit { settings ->
            settings[loginKey] = true
        }
    }

    suspend fun saveToken(token:String){
        context.dataStore.edit { settings ->
            settings[tokenKey] = token
        }
    }

    fun setUserInfo(currentUser: FirebaseUser?) = viewModelScope.launch {
        currentUser?.let{ user->
            setLoginState(
                LoginState.Success.Registered(
                    user.displayName ?: "Name",
                    user.photoUrl
                )
            )
        } ?: kotlin.run{
            setLoginState(
                LoginState.Success.NotRegistered
            )
        }
    }
}