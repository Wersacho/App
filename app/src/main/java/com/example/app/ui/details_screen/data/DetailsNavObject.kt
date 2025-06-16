package com.example.app.ui.details_screen.data

import com.example.app.ui.main_screen.utils.Categories
import kotlinx.serialization.Serializable

//передаем инфу с одного экрана на другой
@Serializable
data class DetailsNavObject(
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val categoryIndex: Int = Categories.ACTION,
    val imageUrl: String = ""
)
