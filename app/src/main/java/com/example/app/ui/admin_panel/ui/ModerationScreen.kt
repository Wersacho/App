package com.example.app.ui.admin_panel.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.app.ui.admin_panel.ModerationScreenViewModel

@Composable
fun ModerationScreen(
    viewModel: ModerationScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getAllComments()
    }

    Box(
        Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            items(viewModel.commentsState.value) { comment ->
                AdminCommentListItem(
                    ratingData = comment,
                    onClickDecline = {
                        viewModel.deleteComment(comment.uid)
                    },
                    onClickAccept = {
                        viewModel.insertModeratedRating(comment)
                    }
                )
                Spacer(
                    Modifier.height(16.dp)
                )
            }
        }
        if (viewModel.commentsState.value.isEmpty()) {
            Text(
                text = "Нет комментариев",
                color = Color.Gray
            )
        }
    }
}