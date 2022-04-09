package app.moc.android.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.moc.android.databinding.HomeMocTalkItemBinding
import app.moc.android.ui.talk.TalkActionHandler

class MocTalkAdapter: PagingDataAdapter<MocTalkItemUIModel, MocTalkAdapter.ViewHolder>(DIFF_CALLBACK) {

    var actionHandler: TalkActionHandler? = null

    inner class ViewHolder(private val binding: HomeMocTalkItemBinding): RecyclerView.ViewHolder(binding.root){
        internal fun bind(uiModel: MocTalkItemUIModel?){
            if(uiModel == null){

            } else {
                binding.uiModel = uiModel
                binding.root.setOnClickListener {
                    actionHandler?.navigateToDetail(uiModel)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(HomeMocTalkItemBinding.inflate(layoutInflater, parent, false))
    }

    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<MocTalkItemUIModel>(){
            override fun areItemsTheSame(
                oldItem: MocTalkItemUIModel,
                newItem: MocTalkItemUIModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: MocTalkItemUIModel,
                newItem: MocTalkItemUIModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}