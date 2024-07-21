package com.rizwan.chatapplication.repository

import androidx.lifecycle.LiveData
import com.rizwan.chatapplication.data.Chat
import com.rizwan.chatapplication.db.ChatDatabase
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val chatDatabase: ChatDatabase
) {
    suspend fun addChat(chat: Chat) {
        chatDatabase.chatDao().addChat(chat)
    }

    fun getChats(): LiveData<List<Chat>> {
        return chatDatabase.chatDao().getChats()
    }
}