package com.rizwan.chatapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat")
data class Chat(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val message: String,
    val time: Long,
    val isSelf: Boolean
)
