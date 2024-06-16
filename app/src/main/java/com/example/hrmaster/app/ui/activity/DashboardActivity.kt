package com.example.hrmaster.app.ui.activity

import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.hrmaster.R
import com.example.hrmaster.data.di.LoginSession
import com.example.hrmaster.databinding.ActivityDashboardBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    @Inject
    lateinit var loginSession: LoginSession
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private var backPressedTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)

        // create nav host
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_dashboard) as NavHostFragment

        // create nav controller
        navController = navHostFragment.navController

        setupWithNavController(binding.bnvAdmin, navController)

        // Force light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Listen for destination changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    if(!(binding.btnFab.isShown)){
                        showFab()
                    }else{
                        animateFab()
                    }
                }
                R.id.reportFragment -> {
                    if(!(binding.btnFab.isShown)){
                        showFab()
                    }else{
                        animateFab()
                    }
                }
                else -> hideFab()
            }
        }

        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val value = "${navHostFragment.childFragmentManager.fragments[0]}".split("{")[0]

        if (value == "HomeFragment") {
            if (backPressedTime + 3000 > System.currentTimeMillis()) {
                super.onBackPressed()
                finish()
            } else {
                Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_LONG).show()
            }
            backPressedTime = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }
    }

    private fun showFab() {
        binding.apply {
            if (!btnFab.isShown) {
                btnFab.show()
            }
        }
    }

    private fun hideFab() {
        binding.apply {
            if (btnFab.isShown) {
                btnFab.hide()
            }
        }
    }

    private fun animateFab() {
        binding.btnFab.clearAnimation()

        // Scale down animation
        val shrink = ScaleAnimation(
            1f,
            0f,
            1f,
            0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        shrink.duration = 200 // Animation duration in milliseconds
        shrink.interpolator = AccelerateInterpolator()
        shrink.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                // Change FAB color and icon if needed
                // Scale up animation
                val expand = ScaleAnimation(
                    0f,
                    1f,
                    0f,
                    1f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                )
                expand.duration = 200 // Animation duration in milliseconds
                expand.interpolator = DecelerateInterpolator()
                binding.btnFab.startAnimation(expand)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        binding.btnFab.startAnimation(shrink)
    }


}