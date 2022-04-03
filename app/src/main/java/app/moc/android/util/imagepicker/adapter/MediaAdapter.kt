package app.moc.android.util.imagepicker.adapter

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.moc.android.util.imagepicker.MocImagePickerBaseBuilder
import app.moc.android.util.imagepicker.listener.OnItemClickListener
import app.moc.android.R
import app.moc.android.databinding.GalleryCameraItemBinding
import app.moc.android.databinding.MediaItemBinding
import app.moc.android.util.imagepicker.model.MediaHeaderUIModel
import app.moc.android.util.imagepicker.model.MediaItemUIModel
import app.moc.android.util.imagepicker.model.MediaUIModel
import app.moc.android.util.imagepicker.model.toModel
import app.moc.android.util.layoutInflater
import com.bumptech.glide.Glide

abstract class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    internal open fun bind(uiModel: MediaUIModel){}
}

internal class MediaAdapter(private val builder: MocImagePickerBaseBuilder<*>, private val itemClickListener: OnItemClickListener) : ListAdapter<MediaUIModel, ViewHolder>(DIFF_CALLBACK)  {

    internal val selectedUris = mutableListOf<Uri>()
    var onMediaAddListener: (() -> Unit)? = null

    inner class ItemViewHolder(private val binding: MediaItemBinding) : ViewHolder(binding.root) {

        override fun bind(uiModel: MediaUIModel){
            if(uiModel !is MediaItemUIModel) return
            binding.root.setOnClickListener {
                itemClickListener.onItemClick(uiModel.toModel(), absoluteAdapterPosition -1, absoluteAdapterPosition)
            }
            Glide.with(binding.imageMedia.context)
                .load(uiModel.uri)
                .thumbnail(0.1f)
                .into(binding.imageMedia)

            binding.run {
                media = uiModel.toModel()
                isSelected = selectedUris.contains(uiModel.uri)
                if(isSelected){
                    selectedNumber = selectedUris.indexOf(uiModel.uri) + 1
                }
            }
        }
    }

    inner class HeaderViewHolder(private val binding: GalleryCameraItemBinding) : ViewHolder(binding.root) {

        override fun bind(uiModel: MediaUIModel){
            if(uiModel !is MediaHeaderUIModel) return
            binding.root.setOnClickListener {
                itemClickListener.onHeaderClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when(viewType){
            R.layout.media_item -> ItemViewHolder(MediaItemBinding.inflate(parent.layoutInflater, parent, false))
            R.layout.gallery_camera_item -> HeaderViewHolder(GalleryCameraItemBinding.inflate(parent.layoutInflater, parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int = when(getItem(position)){
        is MediaItemUIModel -> R.layout.media_item
        is MediaHeaderUIModel -> R.layout.gallery_camera_item
    }

    fun toggleMediaSelect(uri: Uri){
        if(selectedUris.contains(uri)){
            removeMedia(uri)
        } else {
            addMedia(uri)
        }
    }

    private fun addMedia(uri: Uri) {
        if(selectedUris.size == builder.maxCount) {
            val message = builder.maxCountMessage

        } else {
            selectedUris.add(uri)
            onMediaAddListener?.invoke()
            refreshSelectedView()
        }
    }

    private fun refreshSelectedView() {
        selectedUris.forEach {
            val position: Int = getViewPosition(it)
            notifyItemChanged(position)
        }
    }

    private fun getViewPosition(uri: Uri): Int =
        currentList.filterIsInstance(MediaItemUIModel::class.java).indexOfFirst { uiModel -> uiModel.uri == uri } + 1

    private fun removeMedia(uri: Uri) {
        selectedUris.remove(uri)
        val position = getViewPosition(uri)
        notifyItemChanged(position)
        // for remain views which are selected
        refreshSelectedView()
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MediaUIModel>() {
            override fun areItemsTheSame(oldItem: MediaUIModel, newItem: MediaUIModel): Boolean =
                if (oldItem is MediaItemUIModel && newItem is MediaItemUIModel) {
                    oldItem.uri == newItem.uri
                } else if (oldItem is MediaHeaderUIModel && newItem is MediaHeaderUIModel) {
                    oldItem.title == newItem.title
                } else {
                    false
                }

            override fun areContentsTheSame(oldItem: MediaUIModel, newItem: MediaUIModel): Boolean =
                if (oldItem is MediaItemUIModel && newItem is MediaItemUIModel) {
                    oldItem == newItem
                } else if (oldItem is MediaHeaderUIModel && newItem is MediaHeaderUIModel) {
                    oldItem == newItem
                } else {
                    false
                }
        }
    }
}