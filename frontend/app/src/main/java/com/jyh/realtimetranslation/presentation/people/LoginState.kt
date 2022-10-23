package com.jyh.realtimetranslation.presentation.people

import android.net.Uri
import com.jyh.realtimetranslation.data.entity.person.PersonEntity

sealed class LoginState {
    object Uninitialized: LoginState()

    object Loading: LoginState()
    object Error: LoginState()

    data class Login(
        val idToken:String
    ): LoginState()

    sealed class Success: LoginState(){

        data class Registered(
            val userName:String,
            val profileImageUri: Uri?,
            val friends: List<PersonEntity> = listOf()
        ): Success()

        object NotRegistered: Success()
    }
}