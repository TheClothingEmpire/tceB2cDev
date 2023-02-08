package com.clothingempire.clothingempire.ui.activities

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.clothingempire.clothingempire.R
import com.clothingempire.clothingempire.databinding.ActivityDashboardBinding
import com.clothingempire.clothingempire.databinding.ActivityWsdashboardBinding
import java.lang.Exception

class WSDashboardActivity : BaseActivity() {

    private lateinit var binding: ActivityWsdashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWsdashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarDashboard    )
        supportActionBar!!.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.app_gradient_color_background))

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_dashboard)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        /*val appBarConfiguration = AppBarConfiguration(
            setOf(
                 R.id.navigation_products, R.id.navigation_dashboard, R.id.navigation_products
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)*/
        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        doubleBackToExit()
    }
}