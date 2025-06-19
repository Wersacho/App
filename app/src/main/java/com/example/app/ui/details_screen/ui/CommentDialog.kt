package com.example.app.ui.details_screen.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.custom.StarsIndicator
import com.example.app.ui.details_screen.data.RatingData

@Composable
fun CommentDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    ratingData: RatingData,
    confirmButtonText: String = "OK",
) {
    if (showDialog){
        AlertDialog(

            onDismissRequest = {
                onDismiss()
            },


            confirmButton = {

                Button( onClick = {
                    onDismiss()
                }

                ) {
                    Text(
                        text = confirmButtonText
                    )
                }

            },

            title = {
                Column(
                    Modifier
                        .fillMaxWidth()
                ) {
                    StarsIndicator(
                        rating = ratingData.rating
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = ratingData.name,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }


            },

            text = {
                Text(
                    text = ratingData.message,
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
        )
    }
}