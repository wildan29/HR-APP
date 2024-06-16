package com.example.hrmaster.app.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrmaster.data.di.LoginSession
import com.example.hrmaster.data.models.response.GetUserResponse
import com.example.hrmaster.data.repo.MainRepo
import com.example.hrmaster.domain.models.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val mainRepo: MainRepo, private  val loginSession: LoginSession) : ViewModel() {
    private val _getUser =
        MutableStateFlow<Resource<GetUserResponse>>(Resource.loading(null))
    val getUser: StateFlow<Resource<GetUserResponse>>
        get() = _getUser.asStateFlow()

    // create shared flow for handling message
    private val _message = MutableStateFlow<String>("")

    val message: SharedFlow<String>
        get() = _message.asSharedFlow()

    private val _isLaunch = MutableStateFlow(false)

    val isLaunch : StateFlow<Boolean>
        get() = _isLaunch

    private val _getUser3 = MutableSharedFlow<Resource<GetUserResponse>>(replay = 0)
    val getUser3: SharedFlow<Resource<GetUserResponse>>
        get() = _getUser3

    fun launch() = viewModelScope.launch {
        _isLaunch.value = true
    }

    fun getUser() = viewModelScope.launch {
        _getUser.value = Resource.loading(null)

        val token = loginSession.loginSessionFlow.first {
            it.isNotEmpty()
        }

        try {
            mainRepo.getUser(token).let {
                if (it.isSuccessful) {
                    _getUser.value = Resource.success(it.body())
                    _getUser3.emit(Resource.success(it.body()))
                } else {
                    val jsonObj =
                        JSONObject(it.errorBody()!!.charStream().readText()).getString("errors")
                    _message.emit(jsonObj)
                    _getUser.value = (Resource.error(it.errorBody().toString(), null))
                }
            }
        } catch (e: Exception) {
            _getUser.value = (Resource.error(e.message.toString(), null))
            _message.emit(e.message.toString())
            e.printStackTrace()
        }
    }
}