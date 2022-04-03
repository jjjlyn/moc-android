package app.moc.android.util.imagepicker.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.moc.android.databinding.SelectedMediaItemBinding

internal class SelectedMediaAdapter : ListAdapter<Uri, SelectedMediaAdapter.ViewHolder>(DIFF_CALLBACK){

    var onClearClickListener: ((Uri) -> Unit)? = null

    inner class ViewHolder(private val binding: SelectedMediaItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(uri: Uri){
            binding.uri = uri
            binding.imageClear.setOnClickListener {
                onClearClickListener?.invoke(uri)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SelectedMediaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Uri>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean =
                oldItem.toString() == newItem.toString()

            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean =
                oldItem == newItem
        }
    }
}