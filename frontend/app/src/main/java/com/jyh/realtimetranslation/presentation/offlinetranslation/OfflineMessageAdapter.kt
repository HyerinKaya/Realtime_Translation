package com.jyh.realtimetranslation.presentation.offlinetranslation

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jyh.realtimetranslation.R
import com.jyh.realtimetranslation.data.room.offline.OfflineMessageItem
import com.jyh.realtimetranslation.databinding.MessageItemBinding

class OfflineMessageAdapter: ListAdapter<OfflineMessageItem, OfflineMessageAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: MessageItemBinding):RecyclerView.ViewHolder(binding.root){
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(messageItem: OfflineMessageItem){
            binding.messageLayout.background = if(messageItem.isSending) binding.root.context.resources.getDrawable(
                R.drawable.sending_message_background) else binding.root.context.resources.getDrawable(
                R.drawable.received_message_background)
            binding.root.gravity = if(messageItem.isSending) Gravity.END else Gravity.START
            binding.originalText.text = messageItem.originalText
            binding.translatedText.text = messageItem.translatedText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<OfflineMessageItem>(){
            override fun areItemsTheSame(
                oldItem: OfflineMessageItem,
                newItem: OfflineMessageItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: OfflineMessageItem,
                newItem: OfflineMessageItem
            ): Boolean {
                return oldItem.originalText == newItem.originalText
            }

        }
    }
}