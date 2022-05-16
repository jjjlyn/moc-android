package app.moc.android.ui.talk

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.util.IdBasedDiffCallback
import app.moc.android.R
import app.moc.android.databinding.TalkCategoryItemBinding
import app.moc.android.util.getColorCompat
import app.moc.android.util.layoutInflater
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TalkCategoryAdapter(private val lifecycleOwner: LifecycleOwner) : ListAdapter<TalkCategory, TalkCategoryAdapter.ViewHolder>(IdBasedDiffCallback { it.label }) {

    var onCategoryChanged: ((TalkCategory) -> Unit)? = null
    var talkCategory: StateFlow<TalkCategory>? = null

    inner class ViewHolder(private val binding: TalkCategoryItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(category: TalkCategory){
            binding.category = category.label
            binding.root.setOnClickListener {
                onCategoryChanged?.invoke(category)
            }

            lifecycleOwner.lifecycleScope.launch {
                talkCategory
                    ?.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    ?.collectLatest {
                        binding.isSelected = category == it
                        val isSelected = binding.isSelected ?: false
                        with(binding.textCategory){
                            setTextAppearance(
                                if (isSelected) {
                                    R.style.TextAppearance_Moc_H5_2
                                } else {
                                    R.style.TextAppearance_Moc_H5
                                }
                            )
                        }
                    }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TalkCategoryItemBinding.inflate(parent.layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}