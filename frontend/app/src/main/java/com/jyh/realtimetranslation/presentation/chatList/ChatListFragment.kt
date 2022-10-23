package com.jyh.realtimetranslation.presentation.chatList

import android.R
import android.annotation.SuppressLint
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.jyh.realtimetranslation.RealtimeTranslationApplication
import com.jyh.realtimetranslation.data.entity.chatRoom.ChatRoomEntity
import com.jyh.realtimetranslation.data.room.offline.OfflineRoomItem
import com.jyh.realtimetranslation.databinding.FragmentChatListBinding
import com.jyh.realtimetranslation.presentation.BaseFragment
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

internal class ChatListFragment:BaseFragment<ChatListViewModel, FragmentChatListBinding>() {

    // 채팅방 리스트, 어뎁터
    lateinit var offlineRoomListAdapter: OfflineChatListAdapter
    lateinit var onlineRoomListAdapter:  OnlineChatListAdapter
    private val offlineRoomList = mutableListOf<OfflineRoomItem>()
    private val onlineRoomList = mutableListOf<ChatRoomEntity>() // todo 온라인 채팅방 수정 필요

    companion object{
        const val TAG = "ChatListFragment"
    }

    override val viewModel by inject<ChatListViewModel>()

    override fun getViewBinding() = FragmentChatListBinding.inflate(layoutInflater)

    @SuppressLint("NotifyDataSetChanged")
    override fun observeData() {

        // 리사이클러뷰 초기화(대면 만 가능) todo
        offlineRoomListAdapter = OfflineChatListAdapter(requireContext())
        binding.chatListRecyclerView.adapter = offlineRoomListAdapter
        binding.chatListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        (requireActivity().application as RealtimeTranslationApplication).database.offlineRoomDao().getAll().observe(this){
            offlineRoomList.clear()
            offlineRoomList.addAll(it)
            offlineRoomListAdapter.submitList(it)
            offlineRoomListAdapter.notifyDataSetChanged()
        }

        // 스피너 초기화
        initSpinner()

        // 대면, 비대면 상황에서 리스트 띄워주기
        viewModel.isOnline.observe(this){
            if(it){ // 비대면

            }else{ // 대면

            }
        }
    }

    /**
     *  스피터를 초기화해줌
     */
    private fun initSpinner(){
        val chatListSpinnerAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, listOf("대면","비대면"))
        binding.chatListSpinner.adapter = chatListSpinnerAdapter
        binding.chatListSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                initChatListRecyclerView(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    /**
     *
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun initChatListRecyclerView(position: Int) {
        // 대면
        if(position == 0){
            GlobalScope.launch {
                activity?.runOnUiThread {
                    offlineRoomListAdapter.submitList(offlineRoomList)
                }
            }
        }else{
            // 비대면
        }
    }
}