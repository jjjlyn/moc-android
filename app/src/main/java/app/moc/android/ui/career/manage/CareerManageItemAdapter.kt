package app.moc.android.ui.career.manage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.moc.android.databinding.CareerManageItemBinding
import app.moc.android.ui.career.CareerItemUIModel
import app.moc.android.ui.career.CareerManageItemActionHandler

class CareerManageItemAdapter : ListAdapter<CareerItemUIModel, CareerManageItemAdapter.ViewHolder>(
    DIFF_CALLBACK
) {

    var actionHandler: CareerManageItemActionHandler? = null

    inner class ViewHolder(private val binding: CareerManageItemBinding): RecyclerView.ViewHolder(binding.root){
        internal fun bind(uiModel: CareerItemUIModel){
            with(binding){
                binding.root.clipToOutline = true
                actionHandler = this@CareerManageItemAdapter.actionHandler
                this.uiModel = uiModel
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(CareerManageItemBinding.inflate(layoutInflater, parent, false))
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