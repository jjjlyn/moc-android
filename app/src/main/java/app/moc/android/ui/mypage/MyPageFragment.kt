package app.moc.android.ui.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import app.moc.android.R
import app.moc.android.databinding.MyPageFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment: Fragment(R.layout.my_page_fragment) {
    private lateinit var binding: MyPageFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MyPageFragmentBinding.bind(view)
    }
}