package com.example.app.ui.main_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.app.data.Game
import com.example.app.ui.main_screen.bottom_menu.BottomMenuItem
import com.example.app.utils.firebase.FireStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val firestoreManager: FireStoreManager
): ViewModel() {

    val gamesListState = mutableStateOf(emptyList<Game>())
    val isFavListEmptyState = mutableStateOf(false)
    val selectedBottomItemState = mutableStateOf(BottomMenuItem.Home.title)

    fun getAllGames() {
        firestoreManager.getAllGames { games ->
            gamesListState.value = games
            isFavListEmptyState.value = games.isEmpty()
        }
    }

    fun getAllFavsGames() {
        firestoreManager.getAllFavsGames { games ->
            gamesListState.value = games
            isFavListEmptyState.value = games.isEmpty()
        }
    }

    fun getGamesFromCategory(category: String){

        if (category == "Все") {
            getAllGames()
            return
        }

        firestoreManager.getAllGamesFromCategory(category) { games ->
            gamesListState.value = games
            isFavListEmptyState.value = games.isEmpty()
        }
    }

    fun onFavClick(game: Game, isFavState: String) {
        val gamesList = firestoreManager.changeFavState(gamesListState.value, game)
        gamesListState.value = if(isFavState == BottomMenuItem.Favs.title){
            gamesList.filter { it.isFavorite }
        } else {
            gamesList
        }
        isFavListEmptyState.value = gamesListState.value.isEmpty()
    }

}