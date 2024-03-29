package androidx.recyclerview.widget.util

import androidx.recyclerview.widget.DiffUtil

class AlwaysDiffCallback<T> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = false

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = false
}
