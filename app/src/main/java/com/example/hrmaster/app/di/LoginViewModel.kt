package com.example.hrmaster.app.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrmaster.data.di.LoginSession
import com.example.hrmaster.data.models.request.LoginRequest
import com.example.hrmaster.data.models.request.UserRegisterRequest
import com.example.hrmaster.data.models.response.LoginResponse
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
class LoginViewModel @Inject constructor(private val mainRepo: MainRepo,  private val loginSession: LoginSession)  : ViewModel(){
    private val _login =
        MutableStateFlow<Resource<LoginResponse>>(Resource.loading(null))
    val login: StateFlow<Resource<LoginResponse>>
        get() = _login

    // create shared flow for handling message
    private val _message = MutableStateFlow("")

    val message: SharedFlow<String>
        get() = _message

    fun login(request: LoginRequest) = viewModelScope.launch {
        _login.value = Resource.loading(null)

        try {
            mainRepo.login( request).let {
                if (it.isSuccessful) {
                    _login.value = Resource.success(it.body())
                    it.body()?.data?.also { res ->
                        loginSession.updateLoginSession(res.token!!)
                        loginSession.setRole(res.role!!)
                    }
                } else {
                    val jsonObj =
                        JSONObject(it.errorBody()!!.charStream().readText()).getString("errors")
                    _message.value = jsonObj
                    _login.value = (Resource.error(it.errorBody().toString(), null))
                }
            }
        } catch (e: Exception) {
            _login.value = (Resource.error(e.message.toString(), null))
            _message.value = e.message.toString()
            e.printStackTrace()
        }
    }
}