package app.moc.android.ui.talk

import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.util.IdBasedDiffCallback
import app.moc.android.R
import app.moc.android.databinding.TalkCommentItemBinding
import app.moc.android.util.dp
import app.moc.android.util.layoutInflater
import app.moc.android.util.setVisible

class TalkCommentAdapter: ListAdapter<TalkCommentUIModel, TalkCommentAdapter.ViewHolder>(IdBasedDiffCallback { it.commentID.toString() }) {
    var onMoreClick: ((view: View, uiModel: TalkCommentUIModel) -> Unit)? = null

    inner class ViewHolder(private val binding: TalkCommentItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(uiModel: TalkCommentUIModel) {
            with(binding){
                this.uiModel = uiModel
                imageMore.setOnClickListener {
                    onMoreClick?.invoke(it, uiModel)
                }
                divider.setVisible(uiModel.isLastItem.not())
                if(uiModel.hasParent()){
                    root.setPadding(
                        root.context.resources.getDimensionPixelOffset(R.dimen.margin_2xlarge),
                        0.dp().toInt(),
                        0.dp().toInt(),
                        0.dp().toInt()
                    )
                } else {
                    root.setPadding(0.dp().toInt())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TalkCommentItemBinding.inflate(parent.layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}