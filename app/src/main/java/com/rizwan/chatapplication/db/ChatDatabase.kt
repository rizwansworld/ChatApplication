package com.rizwan.chatapplication.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rizwan.chatapplication.data.Chat

@Database(entities = [Chat::class], version = 1)
abstract class ChatDatabase : RoomDatabase() {

    abstract fun chatDao() : ChatDao

}