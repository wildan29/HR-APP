package com.example.hrmaster.app.ui.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.hrmaster.R
import com.example.hrmaster.app.di.MainViewModel
import com.example.hrmaster.data.di.LoginSession
import com.example.hrmaster.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModelSplashScreen by viewModels<MainViewModel>()
    @Inject
    lateinit var loginSession: LoginSession
    private lateinit var navControllerLogin: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModelSplashScreen.isReady.value
            }
//             Add Animation On Exit
            setOnExitAnimationListener { screen ->
                val screenHeight = Resources.getSystem().displayMetrics.heightPixels
                val slideUp = ObjectAnimator.ofFloat(screen.view, View.TRANSLATION_Y, 0f,(-screenHeight.toFloat()/2 + 100) )
                slideUp.interpolator = AnticipateInterpolator()
                slideUp.duration = 1000L
                slideUp.doOnEnd { screen.remove() }

                // start animation
                slideUp.start()
            }
        }
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        // create nav host
        val navHostFragmentLogin =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment

        // create nav controller
        navControllerLogin = navHostFragmentLogin.navController

        // cek if user already login or not
        if (isLogin()){
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        // Force light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navControllerLogin.navigateUp() || super.onSupportNavigateUp()
    }

    private fun isLogin() = runBlocking(Dispatchers.IO) {
        loginSession.loginSessionFlow.first().isNotEmpty()
    }
}