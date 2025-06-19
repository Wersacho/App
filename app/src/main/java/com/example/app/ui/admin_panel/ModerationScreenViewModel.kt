package com.example.app.ui.admin_panel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.ui.details_screen.data.RatingData
import com.example.app.utils.firebase.FireStoreManagerPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModerationScreenViewModel @Inject constructor(
    private val fireStoreManager: FireStoreManagerPaging
): ViewModel() {


    val commentsState = mutableStateOf(emptyList<RatingData>())

    fun insertModeratedRating(
        ratingData: RatingData
    ) = viewModelScope.launch {
        fireStoreManager.deleteComment(ratingData.uid)
        //обновляем локально
        commentsState.value = commentsState.value.filter {
            it.uid != ratingData.uid
        }
        fireStoreManager.insertModeratedRating(ratingData)
    }

    fun deleteComment(
        uid: String
    ) = viewModelScope.launch {
        fireStoreManager.deleteComment(uid)
        //обновляем локально
        commentsState.value = commentsState.value.filter {
            it.uid != uid
        }
    }

    fun getAllComments() = viewModelScope.launch{
        commentsState.value = fireStoreManager.getAllCommentsToModerate()
    }
}