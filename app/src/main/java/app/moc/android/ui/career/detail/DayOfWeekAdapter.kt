package app.moc.android.ui.career.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.moc.android.databinding.CareerDetailDayOfWeekItemBinding
import app.moc.android.ui.career.DayOfWeek
import app.moc.android.util.executeAfter

class DayOfWeekAdapter : ListAdapter<DayOfWeek, DayOfWeekAdapter.ViewHolder>(DIFF_CALLBACK) {

    var onDayOfWeekSelectionChanged: ((String) -> Unit)? = null
    var currentSelections = mutableSetOf<DayOfWeek>()

    inner class ViewHolder(private val binding: CareerDetailDayOfWeekItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: DayOfWeek){
            binding.executeAfter {
                dayOfWeek = item.dayOfWeek
                isSelected = currentSelections.contains(item)
            }
            binding.setOnClick {
                val isSelected = binding.isSelected ?: false
                if (isSelected) {
                    currentSelections.remove(item)
                } else {
                    currentSelections.add(item)
                }
                binding.isSelected = isSelected.not()
                onDayOfWeekSelectionChanged?.invoke(currentSelections.joinToString { it.dayOfWeekEng })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(CareerDetailDayOfWeekItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<DayOfWeek>(){
            override fun areItemsTheSame(
                oldItem: DayOfWeek,
                newItem: DayOfWeek
            ): Boolean {
                return oldItem.dayOfWeek == newItem.dayOfWeek
            }

            override fun areContentsTheSame(
                oldItem: DayOfWeek,
                newItem: DayOfWeek
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}