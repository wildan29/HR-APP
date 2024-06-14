package com.example.hrmaster.data.repo

import com.example.hrmaster.data.models.request.LoginRequest
import com.example.hrmaster.data.models.request.UserRegisterRequest
import com.example.hrmaster.domain.usecase.ApiHelper
import javax.inject.Inject

class MainRepo @Inject constructor(private val apiHelper: ApiHelper) {
    suspend fun userRegiser(request : UserRegisterRequest) = apiHelper.userRegister(request)

    suspend fun login(request: LoginRequest) = apiHelper.login(request)
}