package com.example.app.ui.settings_screen.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.R
import com.example.app.custom.AccountDialog
import com.example.app.data.AccountDialogData
import com.example.app.data.DialogType
import com.example.app.ui.settings_screen.data.CategoryMenuList
import com.example.app.ui.theme.LightRed

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {}
) {
    var dialogData by remember {
        mutableStateOf(AccountDialogData())
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AccountDialog(
            dialogData = dialogData,
            onConfirm = { fieldsValuesList ->
                dialogData = AccountDialogData(showDialog = false)
            },
            onDismiss = {

            }
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = "Николай",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    Modifier.height(6.dp)
                )

                Text(
                    text = "aleoclr@gmail.com"
                )

                Spacer(
                    Modifier.height(6.dp)
                )

                Text(
                    text = "Москва"
                )

                Spacer(
                    Modifier.height(6.dp)
                )

                Text(
                    text = "+7 (900) 823-23-23",
                    color = Color.Gray
                )

                Spacer(
                    Modifier.height(6.dp)
                )


            }
        }



        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .weight(1f)
        ){
            LazyColumn(
                Modifier
                    .fillMaxWidth()
            ) {
                items(CategoryMenuList.menuItemsList) { item ->
                    if (item.isCategory) {
                        MenuCategoryItem(item.title)
                        Spacer(
                            Modifier.height(8.dp)
                        )
                    } else {
                        MenuUiItem(
                            item,
                            onItemClick = { dialogType, title, labelsArrayId ->
                                dialogData = AccountDialogData(
                                    title = title,
                                    dialogType = dialogType,
                                    showDialog = true,
                                    fieldsLabels = context.resources.getStringArray(labelsArrayId).toList()
                                )
                            }
                        )
                    }
                }
            }

        }

        Spacer(
            Modifier.height(16.dp)
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightRed
            ),
            onClick = {

            }
        ) {
            Text(text = "Сохранить")
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            onClick = {
                onBackClick()
            }
        ) {
            Text(text = "Назад")
        }

        Spacer(
            Modifier.height(16.dp)
        )

    }
}