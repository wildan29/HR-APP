package com.example.hrmaster.data.models.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: DataLogin? = null
)

data class DataLogin(

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
