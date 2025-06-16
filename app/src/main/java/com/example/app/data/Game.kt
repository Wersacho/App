package com.example.app.data

import com.example.app.ui.main_screen.utils.Categories

data class Game (
    val key: String = "",
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val category: Int = Categories.ACTION,
    val imageUrl: String = "",
    val isFavorite: Boolean = false
)