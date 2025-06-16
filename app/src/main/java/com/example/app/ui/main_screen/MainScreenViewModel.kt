package com.example.app.ui.main_screen

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.app.data.Game
import com.example.app.ui.main_screen.bottom_menu.BottomMenuItem
import com.example.app.ui.main_screen.utils.Categories
import com.example.app.utils.firebase.FireStoreManagerPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val firestoreManager: FireStoreManagerPaging,

    private val pager: Flow<PagingData<Game>>
): ViewModel() {

    val selectedBottomItemState = mutableIntStateOf(BottomMenuItem.Home.titleId)
    val categoryState = mutableIntStateOf(Categories.ALL)
    var gameToDelete: Game? = null
    private var deleteGame = false


    private val gamesListUpdate = MutableStateFlow<List<Game>>(emptyList())



    //получаем доступ к списку
    val games: Flow<PagingData<Game>> = pager
        .cachedIn(viewModelScope)
        .combine(gamesListUpdate) { pagingData, gamesList ->
            val pgData = pagingData.map { game ->
                val updateGame = gamesList.find {
                    it.key == game.key
                }
                updateGame ?: game
            }

            if (deleteGame) {
                deleteGame = false
                pgData.filter { pgGame ->
                    gamesList.find {
                        it.key == pgGame.key
                    } != null
                }
            } else {
                pgData
            }
        }



    private val _uiState = MutableSharedFlow<MainUiState>()
    //принимаем состояние
    val uiState = _uiState.asSharedFlow()

    private fun sendUiState(state : MainUiState) = viewModelScope.launch {
        //отправляем состояние
        _uiState.emit(state)
    }

    //удаляем игру
    fun deleteGame(uiList: List<Game>){
        if (gameToDelete == null) return
        firestoreManager.deleteGame(
            gameToDelete!!,
            //обновляем локально список игр
            onDeleted = {
                deleteGame = true
                gamesListUpdate.value = uiList.filter {
                    it.key != gameToDelete!!.key
                }
            },
            onFailure = { message ->
                sendUiState(MainUiState.Error(message))
            }
        )
    }

    // отвечает за все
    fun getGamesFromCategory(categoryIndex: Int){
        //вкладка с категорией
        categoryState.intValue = categoryIndex
        firestoreManager.categoryIndex = categoryIndex
    }

    fun onFavClick(game: Game, isFavState: Int, gameList: List<Game>) {
        val gamesList = firestoreManager.changeFavState(gameList, game)
        gamesListUpdate.value = if(isFavState == BottomMenuItem.Favs.titleId){
            deleteGame = true
            gamesList.filter { it.isFavorite }
        } else {
            gamesList
        }
    }

    //3 состояния экрана
    sealed class MainUiState{
        data object Loading: MainUiState()
        data object Success: MainUiState()
        data class Error(val message: String) : MainUiState()
    }

}