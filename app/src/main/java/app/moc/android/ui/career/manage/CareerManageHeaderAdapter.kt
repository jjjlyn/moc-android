package app.moc.android.ui.career.manage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.moc.android.R
import app.moc.android.databinding.ItemComponentTitleBinding
import app.moc.android.ui.common.ComponentTitleUIModel
import app.moc.android.util.executeAfter
import app.moc.android.util.setVisible

class CareerManageHeaderAdapter: ListAdapter<ComponentTitleUIModel, CareerManageHeaderAdapter.ViewHolder>(DIFF_CALLBACK) {

    var onArrowClick: ((Boolean) -> Unit)? = null

    inner class ViewHolder(private val binding: ItemComponentTitleBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(uiModel: ComponentTitleUIModel) {
            with(binding) {
                imageTitle.setVisible(false)
                val isExpanded = "펼치기" == uiModel.button
                executeAfter {
                    this.uiModel = uiModel
                    buttonOption.apply {
                        setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            if (isExpanded) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up,
                            0
                        )
                    }.setVisible("진행중" != uiModel.text)
                }
                setOnClick {
                    onArrowClick?.invoke(isExpanded)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemComponentTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<ComponentTitleUIModel>(){
            override fun areItemsTheSame(
                oldItem: ComponentTitleUIModel,
                newItem: ComponentTitleUIModel
            ): Boolean {
                return oldItem.text == newItem.text
            }

            override fun areContentsTheSame(
                oldItem: ComponentTitleUIModel,
                newItem: ComponentTitleUIModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}