package com.rizwan.chatapplication.di

import android.content.Context
import androidx.room.Room
import com.rizwan.chatapplication.db.ChatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideChatDatabase(@ApplicationContext appContext: Context): ChatDatabase {
        return Room
            .databaseBuilder(appContext, ChatDatabase::class.java, "chatDB")
            .build()
    }
}