package app.moc.android.ui.signup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.moc.android.databinding.KeyWordItemBinding
import app.moc.android.util.executeAfter

class KeyWordsAdapter: ListAdapter<KeyWord, KeyWordsAdapter.ViewHolder>(DIFF_CALLBACK) {

    var currentSelections = mutableSetOf<KeyWord>()

    inner class ViewHolder(private val binding: KeyWordItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: KeyWord){
            binding.executeAfter {
                keyWord = item.content
                isSelected = currentSelections.contains(item)
            }
            binding.setOnClick {
                val isSelected = binding.isSelected ?: false
                if (isSelected) {
                    currentSelections.remove(item)
                } else {
                    currentSelections.add(item)
                }
                binding.isSelected = isSelected.not()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(KeyWordItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<KeyWord>() {
            override fun areItemsTheSame(oldItem: KeyWord, newItem: KeyWord): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: KeyWord, newItem: KeyWord): Boolean {
                return oldItem == newItem
            }
        }
    }
}