package app.moc.android.util.imagepicker.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.util.IdBasedDiffCallback
import app.moc.android.databinding.AlbumItemBinding
import app.moc.android.util.layoutInflater
import app.moc.model.Album

internal typealias OnAlbumItemClickListener = (itemPosition: Int) -> Unit

internal class AlbumAdapter : ListAdapter<Album, AlbumAdapter.ViewHolder>(IdBasedDiffCallback { it.name }) {

    var onAlbumItemClick: OnAlbumItemClickListener? = null
    private var selectedPosition = 0

    fun setSelectedAlbum(album: Album){
        val index = currentList.indexOf(album)
        if(index >= 0 && selectedPosition != index){
            val lastSelectedPosition = selectedPosition
            selectedPosition = index
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
        }
    }

    inner class ViewHolder(private val binding: AlbumItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(album: Album){
            binding.root.setOnClickListener {
                onAlbumItemClick?.invoke(absoluteAdapterPosition)
            }
            binding.album = album
            binding.isSelected = absoluteAdapterPosition == selectedPosition
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AlbumItemBinding.inflate(parent.layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}