package app.moc.android.ui.talk

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.moc.android.databinding.TalkMainFilterHeaderBinding

class TalkFilterAdapter: ListAdapter<TalkFilterUIModel, TalkFilterAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(private val binding: TalkMainFilterHeaderBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(uiModel: TalkFilterUIModel){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TalkMainFilterHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<TalkFilterUIModel>(){
            override fun areItemsTheSame(
                oldItem: TalkFilterUIModel,
                newItem: TalkFilterUIModel
            ): Boolean {
                return oldItem.category == newItem.category && oldItem.filterType == newItem.filterType
            }

            override fun areContentsTheSame(
                oldItem: TalkFilterUIModel,
                newItem: TalkFilterUIModel
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}