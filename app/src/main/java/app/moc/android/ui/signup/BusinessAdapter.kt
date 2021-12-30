package app.moc.android.ui.signup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.moc.android.R
import app.moc.android.databinding.BusinessItemBinding
import app.moc.android.util.executeAfter
import app.moc.model.Business

class BusinessAdapter: ListAdapter<Business, BusinessAdapter.ViewHolder>(DIFF_CALLBACK) {

    var onMainSelectionChanged: ((Business) -> Unit)? = null

    var currentSelection: Business? = null
        set(value){
            if(field == value) {
                return
            }
            val previous = field
            if(previous != null){
                notifyItemChanged(currentList.indexOf(previous)) // deselect previous selection
            }
            field = value
            if (value != null) {
                notifyItemChanged(currentList.indexOf(value)) // select new selection
            }
        }

    inner class ViewHolder(private val binding: BusinessItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Business){
            with(binding){
                executeAfter {
                    this.item = item
                    isSelected = item == currentSelection
                    val isSelected = this.isSelected ?: false
                    textBusiness.setTextAppearance(
                        if(isSelected){
                            R.style.TextAppearance_Moc_H5_3
                        } else {
                            R.style.TextAppearance_Moc_H5
                        }
                    )
                }
                setOnClick {
                    currentSelection = item
                    onMainSelectionChanged?.invoke(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(BusinessItemBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Business>(){
            override fun areItemsTheSame(oldItem: Business, newItem: Business): Boolean {
                return oldItem.categoryM == newItem.categoryM
            }

            override fun areContentsTheSame(oldItem: Business, newItem: Business): Boolean {
                return oldItem == newItem
            }
        }
    }
}