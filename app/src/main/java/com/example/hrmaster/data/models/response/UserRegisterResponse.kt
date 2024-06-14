package com.example.hrmaster.data.models.response

import com.google.gson.annotations.SerializedName

data class UserRegisterResponse(
	@field:SerializedName("data")
	val data: DataRegister? = null
)

data class DataRegister(
	@field:SerializedName("message")
	val message: String? = null
)
