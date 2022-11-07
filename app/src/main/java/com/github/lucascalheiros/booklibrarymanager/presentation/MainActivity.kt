package com.github.lucascalheiros.booklibrarymanager.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.lucascalheiros.booklibrarymanager.R
import com.github.lucascalheiros.booklibrarymanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            val bottomNavigationVisible = destination.id in listOf(
                com.github.lucascalheiros.feature_home.R.id.homeFragment,
                com.github.lucascalheiros.feature_account.R.id.accountFragment
            )
            binding.bottomNavigationView.visibility = if (bottomNavigationVisible) View.VISIBLE else View.GONE
        }

    }

}