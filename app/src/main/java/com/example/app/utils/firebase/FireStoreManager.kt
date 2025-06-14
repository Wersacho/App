package com.example.app.utils.firebase

import com.example.app.data.Favorite
import com.example.app.data.Game
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Singleton

@Singleton
class FireStoreManager(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {

    private fun getAllFavsIds(
        onFavs: (List<String>) -> Unit
    ) {
        getFavsCategoryReference()
            .get()
            .addOnSuccessListener { task ->
                val idsList = task.toObjects(Favorite::class.java)
                val keysList = arrayListOf<String>()
                idsList.forEach {
                    keysList.add(it.key)
                }
                onFavs(keysList)
            }
            .addOnFailureListener {

            }
    }

    fun getAllFavsGames(
        onGames: (List<Game>) -> Unit
    ) {
        getAllFavsIds { idsList ->
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
    }

    fun getAllGamesFromCategory(
        category: String,
        onGames: (List<Game>) -> Unit
    ) {
        getAllFavsIds { idsList ->
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

    }

    fun getAllGames(
        onGames: (List<Game>) -> Unit
    ) {
        getAllFavsIds { idsList ->
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
    }

    //добавляет и удаляет мз избранных
    private fun onFavs(
        favorite: Favorite,
        isFav: Boolean,
    ) {

        val favsDocRef = getFavsCategoryReference()
            .document(favorite.key)

        if (isFav) {
            favsDocRef
                .set( // добавляет
                    favorite
                )
        } else {
            favsDocRef
                .delete() // удаляет
        }
    }

    fun changeFavState(games: List<Game>, game: Game) : List<Game> {
         return games.map { gm ->
            if (gm.key == game.key) {
                onFavs(
                    Favorite(gm.key),
                    !gm.isFavorite
                )
                gm.copy(isFavorite = !gm.isFavorite)
            } else {
                gm
            }
        }
    }

    private fun getFavsCategoryReference(): CollectionReference{
        return db.collection("users")
                    .document(auth.uid ?: "")
                    .collection("favs")
    }

}