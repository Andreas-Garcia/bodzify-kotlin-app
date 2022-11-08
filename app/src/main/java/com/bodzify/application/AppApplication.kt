package com.bodzify.application

import android.app.Application
import com.bodzify.repository.storage.database.AppRoomDatabase
import com.bodzify.repository.PlayRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AppApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { AppRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { PlayRepository(database.playDao()) }
}