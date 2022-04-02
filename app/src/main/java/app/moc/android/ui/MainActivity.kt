package app.moc.android.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import app.moc.android.NavigationHost
import app.moc.android.R
import app.moc.android.databinding.MainActivityBinding
import app.moc.android.ui.MainNavigationAction.*
import app.moc.android.util.setVisible
import app.moc.shared.result.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationHost {

    private lateinit var binding: MainActivityBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    private val onDestinationChanged: (NavController, NavDestination, Bundle?) -> Unit =
        { _, destination, _ ->
            val id = destination.id
            binding.bottomNav.visibility =
                if (id == R.id.home || id == R.id.careerManage) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            with(binding.bottomNav.menu) {
                findItem(id)?.isChecked = true
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Moc_Main)
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater).apply {
            bottomNav.itemIconTintList = null
        }
        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navGraph.startDestination = R.id.loading
        navController.graph = navGraph

        lifecycleScope.launch {
            viewModel.mainDestination
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest { action ->
                    when (action) {
                        is NavigateToHome -> {
                            if (navController.graph.startDestination == R.id.home) return@collectLatest
                            val navOptions =
                                NavOptions.Builder().setPopUpTo(R.id.loading, true).build()
                            navController.navigate(R.id.toHome, null, navOptions)
                            navController.graph.startDestination = R.id.home
                        }
                        is NavigateToOnBoarding -> {
                            val navOptions =
                                NavOptions.Builder().setPopUpTo(R.id.loading, true).build()
                            navController.navigate(R.id.toOnBoarding, null, navOptions)
                            navController.graph.startDestination = R.id.onBoarding
                        }
                    }
            }
        }

        binding.bottomNav.setOnNavigationItemSelectedListener {
            if (it.isChecked) {
                false
            } else {
                NavigationUI.onNavDestinationSelected(it, navController)
                true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(onDestinationChanged)
    }

    override fun onPause() {
        navController.removeOnDestinationChangedListener(onDestinationChanged)
        super.onPause()
    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATIONS)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    companion object {
        private val TOP_LEVEL_DESTINATIONS = setOf(
            R.id.home,
            R.id.careerManage
        )
    }
}