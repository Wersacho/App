package com.example.app.data

data class Game (
    val key: String = "",
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val category: String = "",
    val imageUrl: String = "",
    val isFavorite: Boolean = false
)