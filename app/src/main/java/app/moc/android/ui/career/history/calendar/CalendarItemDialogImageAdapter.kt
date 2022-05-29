package app.moc.android.ui.career.history.calendar

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.moc.android.databinding.CalendarItemDialogImageBinding
import app.moc.android.util.layoutInflater
import app.moc.model.Image

class CalendarItemDialogImageAdapter: ListAdapter<Image, CalendarItemDialogImageAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: CalendarItemDialogImageBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(imageTag: String) {
            binding.apply {
                root.clipToOutline = true
                this.imageTag = imageTag
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Image>() {
            override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CalendarItemDialogImageBinding.inflate(parent.layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position).imageUrl)
    }
}