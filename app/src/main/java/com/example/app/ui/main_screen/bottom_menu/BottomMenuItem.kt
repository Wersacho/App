package com.example.app.ui.main_screen.bottom_menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import com.example.app.R

sealed class BottomMenuItem(
    val route: String,
    //ид ресурса
    val titleId: Int,
    val iconId: Int
) {
    object Home : BottomMenuItem(
        route = "",
        titleId = R.string.home,
        iconId = R.drawable.ic_home
    )

    object Favs : BottomMenuItem(
        route = "",
        titleId = R.string.favs,
        iconId = R.drawable.ic_fav
    )

    object Settings : BottomMenuItem(
        route = "",
        titleId = R.string.settings,
        iconId = R.drawable.ic_settings
    )
}