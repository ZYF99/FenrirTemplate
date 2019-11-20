package com.fenrir.fenrirtemplate.ui.activity

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.fenrir.fenrirtemplate.R
import com.fenrir.fenrirtemplate.databinding.ActivityMainBinding
import com.fenrir.fenrirtemplate.model.sharedpref.SharedPrefModel
import com.fenrir.fenrirtemplate.ui.base.BaseActivity
import com.fenrir.fenrirtemplate.ui.base.SharedViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein


class MainActivity : BaseActivity(), KodeinAware {

    override val kodein by closestKodein()
    private lateinit var binding: ActivityMainBinding
    private val viewModel: SharedViewModel by viewModels()
    private val topDestination by lazy {
        setOf(
            R.id.cameraListFragment
        )
    }
    private var quiteTime: Long = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupNavigation()
    }

    /**
     *  setup Navigation with Toolbar & DrawerLayout
     */

    private fun setupNavigation() {
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration =
            AppBarConfiguration(topDestination, binding.drawerLayout)
//        binding.toolBar.setupWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        binding.cameraListButton.setOnClickListener {
/*            navController.navigate(
                R.id.e51CameraListFragment,
                null,
                navOptions {
                    popUpTo = R.id.e51CameraListFragment
                    launchSingleTop = true
                })*/
            binding.drawerLayout.closeDrawers()
        }
        binding.movieClipButton.setOnClickListener {
/*            navController.navigate(
                R.id.s191MovieClipFragment,
                null,
                navOptions {
                    launchSingleTop = true
                    popUpTo = R.id.s191MovieClipFragment
                })*/
            binding.drawerLayout.closeDrawers()
        }
        binding.userSettingButton.setOnClickListener {
/*            navController.navigate(
                R.id.f61UserSettingFragment,
                null,
                navOptions {
                    popUpTo = R.id.f61UserSettingFragment
                    launchSingleTop = true
                })*/
            binding.drawerLayout.closeDrawers()
        }


        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.drawerLayout.setDrawerLockMode(
                if (topDestination.contains(destination.id))
                    DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
            )
        }

        onBackPressedDispatcher.addCallback(this) {
            when {
                binding.drawerLayout.isDrawerOpen(GravityCompat.START) -> binding.drawerLayout.closeDrawers()
                topDestination.contains(navController.currentDestination?.id) -> {
                    if (System.currentTimeMillis() - quiteTime > 2000) {
                        Toast.makeText(
                            this@MainActivity, R.string.exit_info, Toast.LENGTH_SHORT
                        )
                            .show()
                        quiteTime = System.currentTimeMillis()
                    } else {
                        finish()
                    }
                }
                else -> navController.popBackStack()
            }
        }

        binding.accountNameText.text = SharedPrefModel.email
    }

    fun toggleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawers()
        else binding.drawerLayout.openDrawer(GravityCompat.START)
    }
}
