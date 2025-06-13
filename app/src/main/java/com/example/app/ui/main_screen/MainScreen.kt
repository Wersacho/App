package com.example.app.ui.main_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.app.data.Favorite
import com.example.app.data.Game
import com.example.app.ui.login.data.MainScreenDataObject
import com.example.app.ui.main_screen.bottom_menu.BottomMenu
import com.example.app.ui.main_screen.bottom_menu.BottomMenuItem
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


@Composable
fun MainScreen(
    navData: MainScreenDataObject,
    onGameEditClick: (Game) -> Unit,
    onGameClick: (Game) -> Unit,
    onAdminClick: () -> Unit
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed) // drawer закрыт
    val coroutineScope = rememberCoroutineScope()

    val gamesListState = remember {
        mutableStateOf(emptyList<Game>())
    }

    val selectedBottomItemState = remember {
        mutableStateOf(BottomMenuItem.Home.title)
    }

    val isAdminState = remember {
        mutableStateOf(false)
    }

    val isFavListEmptyState = remember {
        mutableStateOf(false)
    }

    //инициализация бд
    val db = remember { Firebase.firestore }

    //при запуске
    LaunchedEffect(Unit) {
        getAllFavsIds(db, navData.uid) { favs ->
            getAllGames(db, favs) { games ->
                isFavListEmptyState.value = games.isEmpty()
                gamesListState.value = games
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier
            .fillMaxWidth(),
        drawerContent = {
            Column(
                Modifier.fillMaxWidth(0.6f)
            ) {
                DrawerHeader(navData.email)
                DrawerBody(
                    onAdmin = { isAdmin ->
                        isAdminState.value = isAdmin
                    },

                    onFavClick = {
                        selectedBottomItemState.value = BottomMenuItem.Favs.title
                        getAllFavsIds(db, navData.uid) { favs ->
                            getAllFavsGames(db, favs) { games ->
                                isFavListEmptyState.value = games.isEmpty()
                                gamesListState.value = games
                            }
                        }
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    },

                    onAdminClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }

                            onAdminClick()
                    },

                    onCategoryClick = { category ->
                        getAllFavsIds(db, navData.uid) { favs ->
                            if (category == "Все") {
                                getAllGames(db, favs) { games ->
                                    gamesListState.value = games
                                }
                            } else {
                                getAllGamesFromCategory(db, favs, category) { games ->
                                    gamesListState.value = games
                                }
                            }

                        }
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    }
                )
            }

        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomMenu(
                    selectedBottomItemState.value,

                    onFavsClick = {
                        selectedBottomItemState.value = BottomMenuItem.Favs.title
                        getAllFavsIds(db, navData.uid) { favs ->
                            getAllFavsGames(db, favs) { games ->
                                isFavListEmptyState.value = games.isEmpty()
                                gamesListState.value = games
                            }
                        }
                    },

                    onHomeClick = {
                        selectedBottomItemState.value = BottomMenuItem.Home.title
                        getAllFavsIds(db, navData.uid) { favs ->
                            getAllGames(db, favs) { games ->
                                isFavListEmptyState.value = games.isEmpty()
                                gamesListState.value = games
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->

            if (isFavListEmptyState.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Избранное пустое — добавьте игры, \n которые вам нравятся!",
                        color = Color.LightGray,
                        fontSize = 16.sp,
                    )


                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(gamesListState.value) { game ->
                    GameListItemUi(
                        isAdminState.value,
                        game,

                        onGameClick = { gm ->
                            onGameClick(gm)
                        },

                        onEditClick = {
                            onGameEditClick(it)
                        },

                        onFavClick = {
                            gamesListState.value = gamesListState.value.map { gm ->
                                if (gm.key == game.key) {
                                    onFavs(
                                        db,
                                        navData.uid,
                                        Favorite(gm.key),
                                        !gm.isFavorite
                                    )
                                    gm.copy(isFavorite = !gm.isFavorite)
                                } else {
                                    gm
                                }
                            }

                            if (selectedBottomItemState.value == BottomMenuItem.Favs.title) {
                                gamesListState.value = gamesListState.value.filter {
                                    it.isFavorite
                                }
                            }
                        }
                    )
                }
            }
        }
    }

}

private fun getAllGamesFromCategory(
    db: FirebaseFirestore,
    idsList: List<String>,
    category: String,
    onGames: (List<Game>) -> Unit
) {
    db.collection("games")
        .whereEqualTo("category", category)
        .get()
        .addOnSuccessListener { task ->
            val gamesList = task.toObjects(Game::class.java).map {
                if (idsList.contains(it.key)) {
                    it.copy(isFavorite = true)
                } else {
                    it
                }
            }
            onGames(gamesList)
        }
        .addOnFailureListener {

        }
}

private fun getAllGames(
    db: FirebaseFirestore,
    idsList: List<String>,
    onGames: (List<Game>) -> Unit
) {
    db.collection("games")
        .get()
        .addOnSuccessListener { task ->
            val gamesList = task.toObjects(Game::class.java).map {
                if (idsList.contains(it.key)) {
                    it.copy(isFavorite = true)
                } else {
                    it
                }
            }
            onGames(gamesList)
        }
        .addOnFailureListener {

        }
}

private fun getAllFavsGames(
    db: FirebaseFirestore,
    idsList: List<String>,
    onGames: (List<Game>) -> Unit
) {
    if (idsList.isNotEmpty()) {
        db.collection("games")
            .whereIn(FieldPath.documentId(), idsList)
            .get()
            .addOnSuccessListener { task ->
                val gamesList = task.toObjects(Game::class.java).map {
                    if (idsList.contains(it.key)) {
                        it.copy(isFavorite = true)
                    } else {
                        it
                    }
                }
                onGames(gamesList)
            }
            .addOnFailureListener {

            }
    } else {
        onGames(emptyList())
    }
}

private fun getAllFavsIds(
    db: FirebaseFirestore,
    uid: String,
    onFavs: (List<String>) -> Unit
) {
    db.collection("users")
        .document(uid)
        .collection("favs")
        .get()
        .addOnSuccessListener { task ->
            val idsList = task.toObjects(Favorite::class.java)
            val keysList = arrayListOf<String>()
            idsList.forEach{
                keysList.add(it.key)
            }
            onFavs(keysList)
        }
        .addOnFailureListener {

        }
}

//добавляет и удаляет мз избранных
private fun onFavs(
    db: FirebaseFirestore,
    uid: String,
    favorite: Favorite,
    isFav: Boolean,
) {
    if (isFav) {
        db.collection("users")
            .document(uid)
            .collection("favs")
            .document(favorite.key)
            .set( // добавляет
                favorite
            )
    } else {
        db.collection("users")
            .document(uid)
            .collection("favs")
            .document(favorite.key)
            .delete() // удаляет
    }
}
