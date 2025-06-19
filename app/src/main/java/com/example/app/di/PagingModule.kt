package com.example.app.di

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.app.data.Game
import com.example.app.ui.main_screen.data.GameFactoryPaging
import com.example.app.utils.firebase.FireStoreManagerPaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.android.scopes.ViewScoped
import kotlinx.coroutines.flow.Flow

@Module
@InstallIn(ViewModelComponent::class)
object PagingModule {

    @Provides
    @ViewModelScoped
    fun providesPagingFlow(
        fireStoreManagerPaging: FireStoreManagerPaging
    ): Flow<PagingData<Game>> {
        return Pager(
            config = PagingConfig(
                //сколько в одной странице товаров
                pageSize = 6,
                //сколько товаров остается чтобы начать подргрузку
                prefetchDistance = 3,
                //начальный размер страницы при первой загрузке
                initialLoadSize = 9,
            ),
            pagingSourceFactory = {
                GameFactoryPaging(fireStoreManagerPaging)
            }

        ).flow // выдаем через flow
    }

}