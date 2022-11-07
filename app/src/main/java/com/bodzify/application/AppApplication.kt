package com.bodzify.application

import android.app.Application
import com.bodzify.database.AppRoomDatabase
import com.bodzify.repository.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AppApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { AppRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { AppRepository(database.playDao()) }
}