package com.jyh.realtimetranslation.data.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


internal fun provideUsersDB() = Firebase.database.reference.child("Users")

internal fun provideAuth() = FirebaseAuth.getInstance()