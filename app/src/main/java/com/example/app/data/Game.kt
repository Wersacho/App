package com.example.app.data

import com.example.app.ui.main_screen.utils.Categories

data class Game (
    val key: String = "",
    val title: String = "",
    //lowercase search
    val searchTitle: String = title.lowercase(),
    val description: String = "",
    val price: Int = 0,
    val category: Int = Categories.ACTION,
    val imageUrl: String = "",
    val isFavorite: Boolean = false,
    val ratingsList: List<Int> = emptyList()
)