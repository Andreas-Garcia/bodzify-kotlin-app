package com.bodzify.application

import android.app.Application
import com.bodzify.repository.AuthRepository
import com.bodzify.repository.LibraryTrackRepository
import com.bodzify.repository.MineTrackRepository
import com.bodzify.repository.PlayRepository
import com.bodzify.repository.network.api.AuthApi
import com.bodzify.repository.network.api.LibraryTrackApi
import com.bodzify.repository.network.api.MineTrackApi
import com.bodzify.repository.network.api.RemoteDataSource
import com.bodzify.repository.storage.database.AppRoomDatabase
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