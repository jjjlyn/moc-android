package app.moc.android.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
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

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationHost {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: MainActivityBinding

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    companion object {
        private val TOP_LEVEL_DESTINATIONS = emptySet<Int>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Moc_Main)
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navGraph.startDestination = R.id.loading
        navController.graph = navGraph

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mainDestination.collectLatest { action ->
                    when (action) {
                        is NavigateToHome -> {
//                            val navOptions = NavOptions.Builder().setPopUpTo(R.id.loading, true).build()
                            navController.navigate(R.id.toHome)
                            navController.graph.startDestination = R.id.home
                        }
                        is NavigateToOnBoarding -> {
                            navController.navigate(R.id.toOnBoarding)
                            navController.graph.startDestination = R.id.onBoarding
                        }
                    }
                }
            }
        }
    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATIONS)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}