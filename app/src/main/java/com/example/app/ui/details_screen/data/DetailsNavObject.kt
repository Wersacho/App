package com.example.app.ui.details_screen.data

import com.example.app.ui.comments_screen.CommentsNavData
import com.example.app.ui.main_screen.utils.Categories
import kotlinx.serialization.Serializable

//передаем инфу с одного экрана на другой
@Serializable
data class DetailsNavObject(
    val gameId: String= "",
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val categoryIndex: Int = Categories.ACTION,
    val imageUrl: String = "",
    val ratingsList: List<Int> = emptyList()
)

fun DetailsNavObject.toCommentsNavData(): CommentsNavData{
    return CommentsNavData(
        gameId = gameId,
        title = title,
        ratingsList = ratingsList
    )
}
