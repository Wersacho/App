package com.example.app.ui.details_screen.data

data class RatingData (
    val name: String = "",
    val uid: String = "",
    val rating: Int = 5,
    val lastRating: Int = 0,
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val gameId: String = ""
)