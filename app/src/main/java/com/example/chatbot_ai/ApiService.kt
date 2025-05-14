package com.example.chatbot_ai

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("chat")
    suspend fun sendMessage(
        @Field("userMessage") userMessage: String
    ): Response<String>
}