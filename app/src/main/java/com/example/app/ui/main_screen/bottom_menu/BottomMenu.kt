package com.example.app.ui.main_screen.bottom_menu

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.app.ui.theme.BoxFilterColor
import com.example.app.ui.theme.DarkRed
import com.example.app.ui.theme.LightRed

@Composable
fun BottomMenu(
    //string -> int
    selectedItem: Int,
    onFavsClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val items = listOf(
        BottomMenuItem.Home,
        BottomMenuItem.Favs,
        BottomMenuItem.Settings
    )

    NavigationBar(
        containerColor = BoxFilterColor
    ) {

        items.forEach{ item ->
            NavigationBarItem(
                selected = selectedItem == item.titleId,
                onClick = {
                    when (item.titleId) {
                        BottomMenuItem.Home.titleId -> onHomeClick()
                        BottomMenuItem.Favs.titleId -> onFavsClick()
                        BottomMenuItem.Settings.titleId -> onSettingsClick()
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconId),
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = stringResource(item.titleId))
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = DarkRed,
                    indicatorColor = LightRed
                )
            )
        }

    }
}