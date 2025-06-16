package com.example.app.ui.add_game_screen.data

import com.example.app.ui.main_screen.utils.Categories
import kotlinx.serialization.Serializable


@Serializable
data class AddScreenObject(
    val key: String = "",
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val categoryIndex: Int = Categories.ACTION,
    val imageUrl: String = ""
)