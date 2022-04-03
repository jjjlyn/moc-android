package androidx.recyclerview.widget.util

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class IdBasedDiffCallback<T>(
    private val getIdOf: (T) -> String
) : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return getIdOf(oldItem) == getIdOf(newItem)
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}
