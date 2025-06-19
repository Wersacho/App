package com.example.app.ui.admin_panel.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app.ui.login.LoginButton

@Composable
fun AdminPanelScreen(
    onAddGameClick: () -> Unit = {},
    onModerationClick: () -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoginButton(
            text = "Добавить новый товар"
        ) {
            onAddGameClick()
        }

        LoginButton(
            text = "Модерация комментария"
        ) {
            onModerationClick()
        }
    }
}