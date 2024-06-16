package com.example.hrmaster.data.repo

import com.example.hrmaster.data.api.ApiService
import com.example.hrmaster.data.models.request.LoginRequest
import com.example.hrmaster.data.models.request.UserRegisterRequest
import com.example.hrmaster.data.models.response.GetUserResponse
import com.example.hrmaster.data.models.response.LoginResponse
import com.example.hrmaster.data.models.response.UserRegisterResponse
import com.example.hrmaster.domain.usecase.ApiHelper
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {
    override suspend fun userRegister(request: UserRegisterRequest): Response<UserRegisterResponse> {
        return apiService.register(request)
    }

    override suspend fun login(request: LoginRequest): Response<LoginResponse> {
        return apiService.login(request)
    }

    override suspend fun getUser(token: String): Response<GetUserResponse> {
        return apiService.get(token)
    }

}