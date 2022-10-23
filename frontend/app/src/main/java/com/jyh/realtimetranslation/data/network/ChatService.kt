package com.jyh.realtimetranslation.data.network

import com.jyh.realtimetranslation.data.reponse.ChatResponse
import retrofit2.Response
import retrofit2.http.PUT

interface ChatService {
    @PUT("test")
    suspend fun putChat(): Response<ChatResponse>
}