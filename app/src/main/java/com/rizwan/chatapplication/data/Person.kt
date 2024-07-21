package com.rizwan.chatapplication.data

import androidx.annotation.DrawableRes
import com.rizwan.chatapplication.R

data class Person(
    val id: Int = 0,
    val name: String = "",
    @DrawableRes val icon: Int = R.drawable.ic_launcher_background
)