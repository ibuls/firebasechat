package com.firestorechat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.firestorechat.databinding.ExpertChatItemReceivedBinding
import com.firestorechat.databinding.ExpertChatItemSentBinding

class FirebaseChatsAdapter(
    val context: Context,
    val listData: List<ChatModel.ChatMessage>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var userId: Int = 0


    val inflator = LayoutInflater.from(context)
    val VIEW_TYPE_USER = 1
    val VIEW_TYPE_OTHER = 2


    inner class VHSender(val binding: ExpertChatItemSentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setupData(data: ChatModel.ChatMessage) {

            binding.tvMessage.background = ContextCompat.getDrawable(context,R.drawable.corner_16dp) //R.drawable.corner_16dp
            binding.newSepLayout.visibility = View.GONE

            binding.tvMessage.text = data.message
        }
    }

    inner class VHReceiver(val binding: ExpertChatItemReceivedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setupData(data: ChatModel.ChatMessage) {
            binding.newSepLayout.visibility = View.GONE
            binding.detailsLayout.visibility = View.GONE
            binding.tvMessage.text = data.message
        }
    }


    override fun getItemViewType(position: Int) =
        if (listData[position].sentByUser == userId) VIEW_TYPE_USER else VIEW_TYPE_OTHER

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) VHSender(
            DataBindingUtil.inflate(
                inflator,
                R.layout.expert_chat_item_sent,
                parent,
                false
            )
        )
        else VHReceiver(
            DataBindingUtil.inflate(
                inflator,
                R.layout.expert_chat_item_received,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == VIEW_TYPE_USER) {
            (holder as VHSender).setupData(listData[position])
        } else {
            (holder as VHReceiver).setupData(listData[position])
        }
    }
}