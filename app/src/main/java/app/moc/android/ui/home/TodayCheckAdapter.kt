package app.moc.android.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.moc.android.databinding.HomeTodayCheckItemBinding

class TodayCheckAdapter: ListAdapter<TodayCheckItemUIModel, TodayCheckAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(private val binding: HomeTodayCheckItemBinding): RecyclerView.ViewHolder(binding.root){
        internal fun bind(uiModel: TodayCheckItemUIModel){
            binding.root.clipToOutline = true
            binding.uiModel = uiModel
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(HomeTodayCheckItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<TodayCheckItemUIModel>(){
            override fun areItemsTheSame(
                oldItem: TodayCheckItemUIModel,
                newItem: TodayCheckItemUIModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: TodayCheckItemUIModel,
                newItem: TodayCheckItemUIModel
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}