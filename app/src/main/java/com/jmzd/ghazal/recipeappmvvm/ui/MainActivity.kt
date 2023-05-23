package com.jmzd.ghazal.recipeappmvvm.ui

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.jmzd.ghazal.recipeappmvvm.R
import com.jmzd.ghazal.recipeappmvvm.databinding.ActivityMainBinding
import com.jmzd.ghazal.recipeappmvvm.utils.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    //Binding
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    //Other
    private lateinit var navHost: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Setup nav host
        navHost = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        binding.mainBottomNav.background = null
        binding.mainBottomNav.setupWithNavController(navHost.navController)
        //Gone bottom menu
        navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment -> visibilityBottomMenu(false)
                R.id.registerFragment -> visibilityBottomMenu(false)
                else -> visibilityBottomMenu(true)
            }
        }

    }

    override fun onNavigateUp(): Boolean {
        return navHost.navController.navigateUp() || super.onNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun visibilityBottomMenu(isVisibility: Boolean) {
        binding.apply {
            if (isVisibility) {
                /*توی این حالت نوشتن به invisible دسترسی نداریم*/
                mainBottomAppbar.isVisible = true
                mainFabMenu.isVisible = true
            } else {
                mainBottomAppbar.isVisible = false
                mainFabMenu.isVisible = false
            }
        }
    }
}