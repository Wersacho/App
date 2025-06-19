package com.example.app.ui.settings_screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.app.data.DialogType
import com.example.app.ui.settings_screen.data.MenuItem

@Composable
fun MenuUiItem(
    item: MenuItem = MenuItem("Test item"),
    onItemClick: (DialogType, String, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onItemClick(item.dialogType, item.title, item.fieldsLabelsArrayId)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.title,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(
                        start = 28.dp,
                        top = 10.dp,
                        bottom = 10.dp,
                        end = 10.dp
                    ),
                color = Color.Black
            )

            if (item.isSwitchVisible) {
                Switch(
                    checked = item.isChecked,
                    onCheckedChange = {

                    },
                    modifier = Modifier
                        .padding(end = 10.dp)
                )
            }
        }

        /*Text(
            text = item.title,
            modifier = Modifier
                .padding(
                    start = 28.dp,
                    top = 10.dp,
                    bottom = 10.dp,
                    end = 10.dp
                ),
            color = Color.Black
        )*/

        Spacer(
            Modifier
                .padding(
                    horizontal = 16.dp
                )
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )
    }
}