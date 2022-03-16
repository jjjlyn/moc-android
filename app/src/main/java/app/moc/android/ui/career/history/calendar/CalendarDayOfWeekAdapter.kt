package app.moc.android.ui.career.history.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.moc.android.databinding.CalendarDayOfWeekItemBinding

class CalendarDayOfWeekAdapter: ListAdapter<String, CalendarDayOfWeekAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(private val binding: CalendarDayOfWeekItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(dayOfWeek: String){
            binding.textDayOfWeek.text = dayOfWeek
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CalendarDayOfWeekItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}