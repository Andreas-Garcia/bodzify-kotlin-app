package com.bodzify.application

import android.app.Application
import com.bodzify.datasource.network.api.AuthApi
import com.bodzify.datasource.network.api.LibraryTrackApi
import com.bodzify.datasource.network.api.MineTrackApi
import com.bodzify.datasource.network.api.RemoteDataSource
import com.bodzify.datasource.repository.AuthRepository
import com.bodzify.datasource.repository.LibraryTrackRepository
import com.bodzify.datasource.repository.MineTrackRepository
import com.bodzify.datasource.repository.PlayRepository
import com.bodzify.datasource.storage.database.AppRoomDatabase
import com.bodzify.session.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AppApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { AppRoomDatabase.getDatabase(this, applicationScope) }
    val playRepository by lazy { PlayRepository(database.playDao()) }
    val libraryTrackRepository by lazy {
        LibraryTrackRepository(
            RemoteDataSource().buildApi(LibraryTrackApi::class.java, applicationContext),
            SessionManager(applicationContext)
        )
    }
    val mineTrackRepository by lazy {
        MineTrackRepository(
            RemoteDataSource().buildApi(MineTrackApi::class.java, applicationContext),
            SessionManager(applicationContext)
        )
    }
    val authRepository by lazy {
        AuthRepository(
            RemoteDataSource().buildApi(AuthApi::class.java, applicationContext),
            SessionManager(applicationContext))
    }
}