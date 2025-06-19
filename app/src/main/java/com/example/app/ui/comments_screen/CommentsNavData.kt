package com.example.app.ui.comments_screen

import android.icu.text.CaseMap.Title
import com.example.app.ui.details_screen.data.RatingData
import kotlinx.serialization.Serializable

@Serializable
data class CommentsNavData(
    val gameId: String = "",
    val title: String = "",
    val ratingsList: List<Int> = emptyList()
)