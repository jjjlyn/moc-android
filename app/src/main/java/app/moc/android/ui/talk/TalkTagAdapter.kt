package app.moc.android.ui.talk

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.util.IdBasedDiffCallback
import app.moc.android.databinding.TalkTagItemBinding
import app.moc.android.util.layoutInflater

class TalkTagAdapter: ListAdapter<String, TalkTagAdapter.ViewHolder>(IdBasedDiffCallback { it }) {

    inner class ViewHolder(private val binding: TalkTagItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(tag: String){
            binding.editTag.setText(tag)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TalkTagItemBinding.inflate(parent.layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}