package com.example.hrmaster.data.models.response

import com.google.gson.annotations.SerializedName

data class GetUserResponse(

	@field:SerializedName("data")
	val data: DataUser? = null
)

data class DataUser(

	@field:SerializedName("foto")
	val foto: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
