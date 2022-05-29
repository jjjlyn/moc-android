package app.moc.android.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.moc.android.databinding.HomeTodayCheckItemBinding
import app.moc.android.ui.career.CareerItemUIModel
import app.moc.android.ui.career.CareerNavigationHandler

class TodayCheckAdapter: ListAdapter<CareerItemUIModel, TodayCheckAdapter.ViewHolder>(DIFF_CALLBACK) {
    var onItemClick: ((CareerItemUIModel) -> Unit)? = null

    inner class ViewHolder(private val binding: HomeTodayCheckItemBinding): RecyclerView.ViewHolder(binding.root){
        internal fun bind(uiModel: CareerItemUIModel){
            binding.uiModel = uiModel
            binding.root.apply {
                clipToOutline = true
            }.setOnClickListener {
                onItemClick?.invoke(uiModel)
            }
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
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<CareerItemUIModel>(){
            override fun areItemsTheSame(
                oldItem: CareerItemUIModel,
                newItem: CareerItemUIModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CareerItemUIModel,
                newItem: CareerItemUIModel
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}