package com.jyh.realtimetranslation.data.network

import com.jyh.realtimetranslation.data.reponse.PeopleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PeopleAPIService {
    // retro -> firebase
    @GET("people")
    suspend fun getPeople(@Path("userId") userId:String): Response<PeopleResponse>
}