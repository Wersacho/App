package com.example.app.custom

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun MyDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String = "Сбросить пароль",
    message: String = "",
    confirmButtonText: String = "OK",
) {
    if (showDialog){
        AlertDialog(

            onDismissRequest = {
                onDismiss()
            },


            confirmButton = {

                Button( onClick = {
                    onConfirm()
                }

                ) {
                    Text(
                        text = confirmButtonText
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
                    text = title,
                    color = Color.Black,
                    fontSize = 20.sp
                )
            },

            text = {
                Text(
                    text = message,
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
        )
    }
}