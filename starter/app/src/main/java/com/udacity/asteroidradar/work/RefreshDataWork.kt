package com.udacity.asteroidradar.work

import android.content.Context
import android.service.voice.AlwaysOnHotwordDetector
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.Repository.Repository
import com.udacity.asteroidradar.db.AsteroidDataBase.Companion.getInstance
import retrofit2.HttpException

class RefreshDataWork(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWork"
    }

    override suspend fun doWork(): Result {
        val dataBase = getInstance(applicationContext)
        val repository = Repository(dataBase)

        return try {
            repository.fetchAsteroidsOnline()
            repository.fetchImgOfTheDayOnline()
            repository.deledOldDataFromDB()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

}