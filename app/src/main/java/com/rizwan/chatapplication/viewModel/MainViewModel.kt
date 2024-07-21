package com.rizwan.chatapplication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rizwan.chatapplication.data.Chat
import com.rizwan.chatapplication.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    suspend fun addChat(chat: Chat) {
        repository.addChat(chat)
    }

    fun getChats(): LiveData<List<Chat>> {
        return repository.getChats()
    }

}