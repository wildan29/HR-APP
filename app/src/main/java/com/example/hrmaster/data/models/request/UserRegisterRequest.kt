package com.example.hrmaster.data.models.request

import com.google.gson.annotations.SerializedName

data class UserRegisterRequest(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
