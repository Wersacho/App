package com.example.app.ui.add_game_screen

import android.net.Uri
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.Game
import com.example.app.ui.add_game_screen.data.AddScreenObject
import com.example.app.ui.main_screen.MainScreenViewModel.MainUiState
import com.example.app.ui.main_screen.utils.Categories
import com.example.app.utils.firebase.FireStoreManagerPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGameViewModel @Inject constructor(
    private val fireStoreManager: FireStoreManagerPaging
) : ViewModel() {

    val title = mutableStateOf("")
    val description = mutableStateOf("")
    val price =  mutableStateOf("")
    var selectedCategory = mutableIntStateOf(Categories.ACTION)
    val selectedImageUri = mutableStateOf<Uri?>(null)

    private val _uiState = MutableSharedFlow<MainUiState>()
    //принимаем состояние
    val uiState = _uiState.asSharedFlow()

    private fun sendUiState(state : MainUiState) = viewModelScope.launch {
        //отправляем состояние
        _uiState.emit(state)
    }

    fun setDefaultsData(navData: AddScreenObject){
        title.value = navData.title
        description.value = navData.description
        price.value = navData.price
        selectedCategory.intValue = navData.categoryIndex
    }

    fun uploadGame(
        navData: AddScreenObject
    ){
        sendUiState(MainUiState.Loading)
        val game = Game(
            title = title.value,
            description = description.value,
            key = navData.key,
            price = price.value,
            category = selectedCategory.intValue
        )

        fireStoreManager.saveGameImage(
            oldImageUrl = navData.imageUrl,
            uri = selectedImageUri.value,
            game = game,
            onSaved = {
                sendUiState(MainUiState.Success)
            },
            onError = { message ->
                sendUiState(MainUiState.Error(message))
            }
        )
    }
}