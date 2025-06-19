package com.example.app.di

import android.app.Application
import com.example.app.utils.firebase.AuthManager
import com.example.app.utils.firebase.FireStoreManagerPaging
import com.example.app.utils.store.StoreManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.rpc.context.AttributeContext.Auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) //инстанция на уровне приложения
object MainModule {

    @Provides
    @Singleton
    fun provideFirebasePagingManager(
        db: FirebaseFirestore,
        auth: FirebaseAuth,
        storage: FirebaseStorage,
        app: Application
    ): FireStoreManagerPaging {
        return FireStoreManagerPaging(db, auth, storage, app.contentResolver)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideFirebaseFireStore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage{
        return Firebase.storage
    }

    @Provides
    @Singleton
    fun provideAuthManager(
        auth: FirebaseAuth,
    ): AuthManager {
        return AuthManager(auth)
    }

    // инициализация storemanager
    @Provides
    @Singleton
    fun provideStoreManager(
        app: Application
    ): StoreManager {
        return StoreManager(app)
    }

}