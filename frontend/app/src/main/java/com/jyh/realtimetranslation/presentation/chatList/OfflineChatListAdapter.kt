package com.jyh.realtimetranslation.presentation.chatList

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jyh.realtimetranslation.data.room.offline.OfflineRoomItem
import com.jyh.realtimetranslation.databinding.OfflineRoomItemBinding
import com.jyh.realtimetranslation.presentation.offlinetranslation.OfflineTranslationActivity
import java.util.*


class OfflineChatListAdapter(private val context: Context): ListAdapter<OfflineRoomItem, OfflineChatListAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(val binding: OfflineRoomItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item: OfflineRoomItem){
            binding.root.setOnClickListener {
                val intent = Intent(context, OfflineTranslationActivity::class.java)
                intent.putExtra("roomId", item.roomId)
                context.startActivity(intent)
            }
            binding.offlineRoomNameTextView.text = Date(item.roomId).toString()
            binding.offlineRoomPreviewTextView.text = item.preview
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(OfflineRoomItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<OfflineRoomItem>(){
            override fun areItemsTheSame(
                oldItem: OfflineRoomItem,
                newItem: OfflineRoomItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: OfflineRoomItem,
                newItem: OfflineRoomItem
            ): Boolean {
                return oldItem.roomId == newItem.roomId
            }

        }
    }
}