package com.example.hrmaster.app.di

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrmaster.data.models.request.UserRegisterRequest
import com.example.hrmaster.data.models.response.UserRegisterResponse
import com.example.hrmaster.data.repo.MainRepo
import com.example.hrmaster.domain.models.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val mainRepo: MainRepo)  : ViewModel(){
    private val _userSignUp =
        MutableStateFlow<Resource<UserRegisterResponse>>(Resource.loading(null))
    val userSignUp: StateFlow<Resource<UserRegisterResponse>>
        get() = _userSignUp

    // create shared flow for handling message
    private val _message = MutableStateFlow("")

    val message: SharedFlow<String>
        get() = _message

    fun add(request: UserRegisterRequest) = viewModelScope.launch {
        _userSignUp.value = Resource.loading(null)

        try {
            mainRepo.userRegiser( request).let {
                if (it.isSuccessful) {
                    _userSignUp.value = Resource.success(it.body())
                } else {
                    val jsonObj =
                        JSONObject(it.errorBody()!!.charStream().readText()).getString("errors")
                    _message.value = jsonObj
                    _userSignUp.value = (Resource.error(it.errorBody().toString(), null))
                }
            }
        } catch (e: Exception) {
            _userSignUp.value = (Resource.error(e.message.toString(), null))
            _message.value = e.message.toString()
            e.printStackTrace()
        }
    }
}