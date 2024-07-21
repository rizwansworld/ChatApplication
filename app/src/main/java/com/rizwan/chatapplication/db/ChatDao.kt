package com.rizwan.chatapplication.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rizwan.chatapplication.data.Chat

@Dao
interface ChatDao {
    @Insert
    suspend fun addChat(chat: Chat)

    @Query("SELECT * FROM chat")
    fun getChats() : LiveData<List<Chat>>
}