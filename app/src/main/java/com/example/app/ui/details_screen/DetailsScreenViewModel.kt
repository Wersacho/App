package com.example.app.ui.details_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.ui.details_screen.data.RatingData
import com.example.app.utils.firebase.FireStoreManagerPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    private val fireStoreManager: FireStoreManagerPaging
): ViewModel() {


    val commentsState = mutableStateOf(emptyList<RatingData>())
    val ratingDataState = mutableStateOf<RatingData?>(RatingData())

    fun insertRating(
        ratingData: RatingData,
        gameId: String
    ){
        fireStoreManager.insertUserComment(ratingData, gameId)
    }

    fun getGameComments(gameId: String) = viewModelScope.launch {
        commentsState.value = fireStoreManager.getGameComments(gameId)
    }

    fun getUserRating(gameId: String) = viewModelScope.launch {
        ratingDataState.value = fireStoreManager.getUserRating(gameId)
    }
}