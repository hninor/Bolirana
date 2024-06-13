package com.hninor.adminbolirana.data

import androidx.annotation.WorkerThread
import com.hninor.adminbolirana.domain.Chico
import javax.inject.Inject

class ChicoRepository @Inject constructor(private val wordDao: ChicoDBDao) {


    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: ChicoDB) {
        wordDao.insert(word)
    }
}