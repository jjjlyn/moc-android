package app.moc.android.ui.career.manage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemMarginDecoration
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.moc.android.R
import app.moc.android.databinding.CareerManageListItemBinding
import app.moc.android.ui.career.CareerManageItemActionHandler
import app.moc.android.ui.career.CareerManageListItemUIModel
import app.moc.android.ui.common.ComponentTitleUIModel
import app.moc.android.util.setVisible

class CareerManageAdapter : ListAdapter<CareerManageListItemUIModel, CareerManageAdapter.ViewHolder>(
    DIFF_CALLBACK
) {
    var actionHandler: CareerManageItemActionHandler? = null

    inner class ViewHolder(private val binding: CareerManageListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val careerManageItemAdapter = CareerManageItemAdapter().apply {
            actionHandler = this@CareerManageAdapter.actionHandler
        }

        internal fun bind(uiModel: CareerManageListItemUIModel) {
            binding.apply {
                with(containerTitle){
                    this.uiModel = ComponentTitleUIModel(text = uiModel.title, button = "접기")
                    buttonOption.apply {
                        setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
                    }.setVisible("진행중" != uiModel.title)

                    setOnClick {
                        listCareerManage.isVisible = listCareerManage.isVisible.not()
                        this.uiModel = ComponentTitleUIModel(text = uiModel.title, button = if(listCareerManage.isVisible) "접기" else "펼치기")
                        buttonOption.apply {
                            setCompoundDrawablesWithIntrinsicBounds(
                                0, 0, if (listCareerManage.isVisible) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down, 0
                            )
                        }
                    }
                }
                listCareerManage.adapter = careerManageItemAdapter
                listCareerManage.addItemDecoration(ItemMarginDecoration(
                    horizontal = root.resources.getDimensionPixelOffset(R.dimen.margin_large),
                    vertical = root.resources.getDimensionPixelOffset(R.dimen.spacing_micro)
                ))
            }
            careerManageItemAdapter.submitList(uiModel.list)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(CareerManageListItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CareerManageListItemUIModel>() {
            override fun areItemsTheSame(
                oldItem: CareerManageListItemUIModel,
                newItem: CareerManageListItemUIModel
            ): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(
                oldItem: CareerManageListItemUIModel,
                newItem: CareerManageListItemUIModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}