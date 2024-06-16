package com.example.hrmaster.domain.usecase

import com.example.hrmaster.data.models.request.LoginRequest
import com.example.hrmaster.data.models.request.UserRegisterRequest
import com.example.hrmaster.data.models.response.GetUserResponse
import com.example.hrmaster.data.models.response.LoginResponse
import com.example.hrmaster.data.models.response.UserRegisterResponse
import retrofit2.Response

interface ApiHelper {
    suspend fun userRegister(request: UserRegisterRequest ): Response<UserRegisterResponse>

    suspend fun login(request: LoginRequest) : Response<LoginResponse>

    suspend fun getUser(token : String):Response<GetUserResponse>
}