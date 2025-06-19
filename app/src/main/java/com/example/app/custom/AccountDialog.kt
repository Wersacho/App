package com.example.app.custom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.app.data.AccountDialogData

@Composable
fun AccountDialog(
    dialogData: AccountDialogData,
    onDismiss: () -> Unit,
    onConfirm: (List<String>) -> Unit
) {
    if (dialogData.showDialog){
        AlertDialog(

            onDismissRequest = {
                onDismiss()
            },


            confirmButton = {

                Button( onClick = {
                    onConfirm(emptyList())
                }

                ) {
                    Text(
                        text = "OK"
                    )
                }

                Button( onClick = {
                    onDismiss()
                }

                ) {
                    Text(
                        text = "Отменить"
                    )
                }
            },

            title = {
                Text(
                    text = dialogData.title,
                    color = Color.Black,
                    fontSize = 20.sp
                )
            },

            text = {
                Column(
                    Modifier.fillMaxWidth()
                ) {
                    dialogData.fieldsLabels.forEach{ name ->
                        TextField(
                            value = "",
                            onValueChange = {

                            },
                            label = {
                                Text(text = name)
                            }
                        )
                    }
                }

            }
        )
    }
}