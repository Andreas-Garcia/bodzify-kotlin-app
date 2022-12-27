package com.bodzify.application

import android.app.Application
import com.bodzify.datasource.network.api.*
import com.bodzify.datasource.repository.*
import com.bodzify.datasource.storage.database.AppRoomDatabase
import com.bodzify.session.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AppApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { AppRoomDatabase.getDatabase(this, applicationScope) }
    private val sessionManager by lazy { SessionManager(applicationContext)}
    val playedTrackRepository by lazy { PlayedTrackRepository(database.playedTrackDao()) }
    val playedPlaylistRepository by lazy { PlayedPlaylistRepository(database.playedPlaylistDao()) }
    val libraryTrackRepository by lazy {
        LibraryTrackRepository(
            RemoteDataSource().buildApi(LibraryTrackApi::class.java, sessionManager),
            sessionManager
        )
    }
    val mineTrackRepository by lazy {
        MineTrackRepository(
            RemoteDataSource().buildApi(MineTrackApi::class.java, sessionManager),
            sessionManager
        )
    }
    val playlistRepository by lazy {
        PlaylistRepository(
            RemoteDataSource().buildApi(PlaylistApi::class.java, sessionManager),
            sessionManager
        )
    }
    val genreRepository by lazy {
        GenreRepository(
            RemoteDataSource().buildApi(GenreApi::class.java, sessionManager),
            sessionManager
        )
    }
    val authRepository by lazy {
        AuthRepository(
            RemoteDataSource().buildApi(AuthApi::class.java, sessionManager),
            sessionManager
        )
    }
}