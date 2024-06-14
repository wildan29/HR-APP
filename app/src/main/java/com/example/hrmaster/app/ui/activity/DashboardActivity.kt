package com.example.hrmaster.app.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

}