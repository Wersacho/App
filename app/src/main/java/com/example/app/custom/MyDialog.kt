package com.example.app.custom

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.app.R


@Composable
fun MyDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String = stringResource(R.string.reset_password),
    message: String = "",
    confirmButtonText: String = stringResource(R.string.confirm),
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
                        text = stringResource(R.string.cancel)
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