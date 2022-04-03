package app.moc.android.ui.career.check

import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.moc.android.databinding.CareerCheckItemBinding
import app.moc.android.util.layoutInflater

class CareerCheckAdapter: ListAdapter<Uri, CareerCheckAdapter.ViewHolder>(DIFF_CALLBACK){

    internal var selectedUris = mutableListOf<Uri>()
    internal var clearClickListener : ((Uri) -> Unit)? = null

    inner class ViewHolder(private val binding: CareerCheckItemBinding): RecyclerView.ViewHolder(binding.root){
        internal fun bind(uri: Uri){
            binding.uri = uri
            binding.imageClear.setOnClickListener {
                clearClickListener?.invoke(uri)
            }
        }
    }

    internal fun clearUriSelect(uri: Uri){
        if(selectedUris.contains(uri)){
            removeUri(uri)
        }
    }

    private fun removeUri(uri: Uri) {
        selectedUris.remove(uri)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(CareerCheckItemBinding.inflate(parent.layoutInflater, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Uri>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean =
                oldItem == newItem
        }
    }
}