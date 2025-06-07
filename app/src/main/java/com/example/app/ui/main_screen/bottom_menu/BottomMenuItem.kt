package com.example.app.ui.main_screen.bottom_menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import com.example.app.R

sealed class BottomMenuItem(
    val route: String,
    val title: String,
    val iconId: Int
) {
    object Home : BottomMenuItem(
        route = "",
        title = "Главная",
        iconId = R.drawable.ic_home
    )

    object Favs : BottomMenuItem(
        route = "",
        title = "Избранное",
        iconId = R.drawable.ic_fav
    )

    object Settings : BottomMenuItem(
        route = "",
        title = "Настройки",
        iconId = R.drawable.ic_settings
    )
}