package com.example.hrmaster.data.api

import com.example.hrmaster.data.models.request.LoginRequest
import com.example.hrmaster.data.models.request.UserRegisterRequest
import com.example.hrmaster.data.models.response.LoginResponse
import com.example.hrmaster.data.models.response.UserRegisterResponse
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/user/register")
    suspend fun register(
        @Body register: UserRegisterRequest
    ): Response<UserRegisterResponse>

    @POST("api/user/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

}