package app.moc.android

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

interface NavigationHost {
    fun registerToolbarWithNavigation(toolbar: Toolbar)
}

interface NavigationDestination {
    fun onUserInteraction() {}
}

open class MainNavigationFragment(@LayoutRes contentLayoutId: Int): Fragment(contentLayoutId), NavigationDestination {

    protected var navigationHost: NavigationHost? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is NavigationHost){
            navigationHost = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        navigationHost = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val host = navigationHost ?: return
        val mainToolbar: Toolbar = view.findViewById(R.id.header) ?: return
        mainToolbar.apply {
            host.registerToolbarWithNavigation(this)
        }
    }
}