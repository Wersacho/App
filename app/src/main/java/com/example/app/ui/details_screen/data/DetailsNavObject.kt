package com.example.app.ui.details_screen.data

import android.icu.text.CaseMap.Title
import kotlinx.serialization.Serializable

//передаем инфу с одного экрана на другой
@Serializable
data class DetailsNavObject(
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val category: String = "",
    val imageUrl: String = ""
)
