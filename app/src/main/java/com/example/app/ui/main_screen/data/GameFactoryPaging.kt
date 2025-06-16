package com.example.app.ui.main_screen.data

import android.util.Log
import androidx.compose.animation.core.snap
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.app.data.Game
import com.example.app.utils.firebase.FireStoreManagerPaging
import com.google.firebase.firestore.DocumentSnapshot
import okio.IOException
import javax.inject.Inject

//страница - documentsnapshot, элемент - game
class GameFactoryPaging @Inject constructor(
    private val fireStoreManagerPaging: FireStoreManagerPaging
): PagingSource<DocumentSnapshot, Game>() {
    override fun getRefreshKey(state: PagingState<DocumentSnapshot, Game>)
    : DocumentSnapshot? {
        //начинаем подгрузку с первой страницы
        return null
    }

    //делаем запрос и возвращаем ответ в виде LoadResult
    override suspend fun load(params: LoadParams<DocumentSnapshot>)
    : LoadResult<DocumentSnapshot, Game> {

        //отслеживаем ошибки
        try {

            val currentPage = params.key
            Log.d("MyLog", "Current page: $currentPage")
            val (snapshot, games) = fireStoreManagerPaging.nextPage(
                pageSize = params.loadSize.toLong(),
                currentKey = currentPage
            )
            Log.d("MyLog", "Game lsit size: ${games.size}")
            val prevKey = if (currentPage == null) {
                null
            } else {
                snapshot.documents.firstOrNull()
            }
            val nextKey = snapshot.documents.lastOrNull()
            Log.d("MyLog", "Prev key: ${prevKey?.id}")
            Log.d("MyLog", "Next key: ${nextKey?.id}")

            return LoadResult.Page(
                data = games,
                prevKey = prevKey,
                nextKey = nextKey
            )

        } catch (
            e: IOException
        ) {
            return LoadResult.Error(e)
        }
    }
}