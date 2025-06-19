package com.example.app.utils.firebase

import android.content.ContentResolver
import android.net.Uri
import com.example.app.data.Favorite
import com.example.app.data.Game
import com.example.app.ui.details_screen.data.RatingData
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
    private val storage: FirebaseStorage,
    private val contentResolver: ContentResolver
) {
    var categoryIndex = Categories.ALL
    var searchText = ""

    var filterData = FilterData()

    suspend fun nextPage(
        pageSize: Long,
        currentKey: DocumentSnapshot?
    ): Pair<QuerySnapshot, List<Game>> {
        var query: Query = db
            .collection(FirebaseConst.GAMES)
            .limit(pageSize)
            .orderBy(filterData.filterType)

        val keysFavsList = getIdsFavsList()

        query = when(categoryIndex){
            Categories.ALL -> query
            Categories.FAVORITES -> query.whereIn(FieldPath.of(FirebaseConst.KEY), keysFavsList)
            else -> query.whereEqualTo(FirebaseConst.CATEGORY, categoryIndex)
        }

        if (searchText.isNotEmpty()){
            //searchTitle - lowercase
            query = query.whereGreaterThanOrEqualTo(FirebaseConst.SEARCH_TITLE, searchText.lowercase())
                .whereLessThan(FirebaseConst.SEARCH_TITLE, "${searchText.lowercase()}\uF7FF") // uF7FF - строка не закончена
        }

        if(filterData.filterType == FirebaseConst.PRICE
            && filterData.minPrice != 0
            && filterData.maxPrice != 0
            && filterData.minPrice <= filterData.maxPrice) {

            query = query.whereGreaterThanOrEqualTo(FirebaseConst.PRICE, filterData.minPrice)
                .whereLessThanOrEqualTo(FirebaseConst.PRICE, filterData.maxPrice)

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
        return db.collection(FirebaseConst.USERS)
            .document(auth.uid ?: "")
            .collection(FirebaseConst.FAVS)
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
        db.collection(FirebaseConst.GAMES)
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
        val db = db.collection(FirebaseConst.GAMES)
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

    fun uploadImageToStorage(
        oldImageUrl: String,
        uri: Uri?,
        game: Game,
        onSaved: () -> Unit,
        onError: (String) -> Unit
    ) {
        val timeStamp = System.currentTimeMillis()
        val storageRef = if(oldImageUrl.isEmpty()) {
            storage.reference
                .child(FirebaseConst.GAME_IMAGES)
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

    fun insertUserComment(
        ratingData: RatingData,
        gameId: String
    ) {
        if (auth.uid == null) return
        db.collection(FirebaseConst.MODERATION_RATING)
            .document(auth.uid!!)
            .set(ratingData.copy(
                name = auth.currentUser?.email ?: "Unknown",
                uid = auth.uid!!,
                gameId = gameId
            ))

    }

    suspend fun insertModeratedRating(
        ratingData: RatingData
    ) {
        if (auth.uid == null) return
        db.collection(FirebaseConst.GAMES_RATING)
            .document(ratingData.gameId)
            .collection(FirebaseConst.RATING)
            .document(ratingData.uid)
            .set(ratingData)

        val game : Game = db.collection(FirebaseConst.GAMES)
            .document(ratingData.gameId)
            .get().await().toObject(Game::class.java) ?: return

        val ratingsList = game.ratingsList.toMutableList()
        if (ratingData.lastRating == 0){
            ratingsList.add(ratingData.rating)
        } else {
            val index = ratingsList.indexOf(ratingData.lastRating)
            ratingsList[index] = ratingData.rating
        }

        db.collection(FirebaseConst.GAMES)
            .document(ratingData.gameId)
            .update("ratingsList", ratingsList)

    }

    suspend fun getAllCommentsToModerate() : List<RatingData> {

        val querySnapshot = db.collection(FirebaseConst.MODERATION_RATING)
            .get().await()

        val commentsList = querySnapshot.toObjects(RatingData::class.java)

        return commentsList
    }

    suspend fun deleteComment(uid: String){
        db.collection(FirebaseConst.MODERATION_RATING)
            .document(uid)
            .delete().await()
    }

    suspend fun getGameComments(
        gameId: String
    ) : List<RatingData> {

        val querySnapshot = db.collection(FirebaseConst.GAMES_RATING)
            .document(gameId)
            .collection(FirebaseConst.RATING)
            .get().await()

        return querySnapshot.toObjects(RatingData::class.java)
    }

    suspend fun getUserRating(
        gameId: String
    ) : RatingData? {
        if (auth.uid == null) return null
        val querySnapshot = db.collection(FirebaseConst.GAMES_RATING)
            .document(gameId)
            .collection(FirebaseConst.RATING)
            .document(auth.uid!!)
            .get().await()

        return querySnapshot.toObject(RatingData::class.java)
    }

}