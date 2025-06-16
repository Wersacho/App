package com.example.app.utils.firebase

import android.net.Uri
import com.example.app.data.Favorite
import com.example.app.data.Game
import com.example.app.ui.main_screen.utils.Categories
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Singleton
class FireStoreManagerPaging(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) {
    var categoryIndex = Categories.ALL

    suspend fun nextPage(
        pageSize: Long,
        currentKey: DocumentSnapshot?
    ): Pair<QuerySnapshot, List<Game>> {
        var query: Query = db.collection("games").limit(pageSize)

        val keysFavsList = getIdsFavsList()

        query = when(categoryIndex){
            Categories.ALL -> query
            Categories.FAVORITES -> query.whereIn(FieldPath.documentId(), keysFavsList)
            else -> query.whereEqualTo("category", categoryIndex)
        }

        if (currentKey != null){
            query = query.startAfter(currentKey)
        }
        val querySnapshot = query.get().await()
        val games = querySnapshot.toObjects(Game::class.java)
        val updatedGames = games.map {
            if (keysFavsList.contains(it.key)) {
                it.copy(isFavorite = true)
            } else {
                it
            }
        }
        return Pair(querySnapshot, updatedGames)
    }

    private suspend fun getIdsFavsList(): List<String> {
        val snapshot = getFavsCategoryReference().get().await()
        val idsList = snapshot.toObjects(Favorite::class.java)
        val keysList = arrayListOf<String>()

        idsList.forEach {
            keysList.add(it.key)
        }
        return if (keysList.isEmpty()) listOf("-1") else keysList
    }

    //получаем ид избранных
    private fun getFavsCategoryReference(): CollectionReference{
        return db.collection("users")
            .document(auth.uid ?: "")
            .collection("favs")
    }

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

    fun deleteGame(
        game: Game,
        onDeleted: () -> Unit,
        onFailure: (String) -> Unit
    ){
        db.collection("games")
            .document(game.key)
            .delete()
            //узнаем что удалили из списка
            .addOnSuccessListener {
                onDeleted()
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Ошибка удаления товара")
            }
    }

    private fun saveGameToFireStore(
        game: Game,
        onSaved: () -> Unit,
        onError: (String) -> Unit
    ) {
        val db = db.collection("games")
        val key = game.key.ifEmpty { db.document().id }
        db.document(key)
            .set(
                game.copy(
                    key = key,
                )
            )
            .addOnSuccessListener {
                onSaved()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Ошибка сохранения товара")
            }
    }

    fun saveGameImage(
        oldImageUrl: String,
        uri: Uri?,
        game: Game,
        onSaved: () -> Unit,
        onError: (String) -> Unit
    ) {
        val timeStamp = System.currentTimeMillis()
        val storageRef = if(oldImageUrl.isEmpty()) {
            storage.reference
                .child("game_images")
                .child("image_$timeStamp.jpg")
        } else {
            storage.getReferenceFromUrl(oldImageUrl)
        }
        if (uri == null) {
            saveGameToFireStore(
                game.copy(imageUrl = oldImageUrl),
                onSaved = {
                    onSaved()
                },
                onError = { message ->
                    onError(message)
                }
            )
            return
        }
        val uploadTask = storageRef.putFile(uri)
        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { url ->
                saveGameToFireStore(
                    game.copy(imageUrl = url.toString()),
                    onSaved = {
                        onSaved()
                    },
                    onError = { message ->
                        onError(message)
                    }
                )
            }
        }
    }

}