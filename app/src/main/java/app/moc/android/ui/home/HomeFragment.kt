package app.moc.android.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import app.moc.android.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.home_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}