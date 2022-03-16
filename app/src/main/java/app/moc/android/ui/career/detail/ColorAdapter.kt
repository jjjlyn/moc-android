package app.moc.android.ui.career.detail

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.moc.android.databinding.ColorItemBinding
import app.moc.android.util.executeAfter

class ColorAdapter: ListAdapter<String, ColorAdapter.ViewHolder>(DIFF_CALLBACK) {

    var currentSelection: String = "#2562FF"
        set(value){
            if(field == value) {
                return
            }
            val previous = field
            notifyItemChanged(currentList.indexOf(previous))
            field = value
            notifyItemChanged(currentList.indexOf(value))
        }

    inner class ViewHolder(private val binding: ColorItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(color: String){
            with(binding){
                executeAfter {
                    this.color = Color.parseColor(color)
                    isSelected = color == currentSelection
                }
                setOnClick {
                    currentSelection = color
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(ColorItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>(){
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}