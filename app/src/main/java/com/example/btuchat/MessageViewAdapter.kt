package com.example.btuchat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import kotlin.collections.ArrayList

class MessageViewAdapter : RecyclerView.Adapter<MessageViewAdapter.MyViewHolder>() {

    private val messages = ArrayList<Message>()

    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        internal var userEmail: TextView
        internal var userMessage: TextView

        init {
            userEmail = v.findViewById(R.id.userEmail)
            userMessage = v.findViewById(R.id.userMessage)
        }

        internal fun setData(message: Message) {
            this.userEmail.text = message.sender
            this.userMessage.text = message.message
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.message_cell, parent, false)

        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(messages[position])
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun addMessage(messages: List<Message>) {
        this.messages.clear()
        this.messages.addAll(messages)
        notifyDataSetChanged()
    }
}
