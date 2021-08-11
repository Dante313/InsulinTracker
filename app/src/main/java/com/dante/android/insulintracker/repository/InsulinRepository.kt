package com.dante.android.insulintracker.repository

import androidx.lifecycle.LiveData
import com.dante.android.insulintracker.data.InsulinDao
import com.dante.android.insulintracker.model.Insulin

class InsulinRepository(private val insulinDao: InsulinDao) {

    val readAllData: LiveData<List<Insulin>> = insulinDao.readAllData()

    suspend fun addInsulin(insulin: Insulin) {
        insulinDao.addInsulin(insulin)
    }

    suspend fun updateInsulin(insulin: Insulin) {
        insulinDao.updateInsulin(insulin)
    }

    suspend fun deleteInsulin(insulin: Insulin) {
        insulinDao.deleteInsulin(insulin)
    }

    suspend fun deleteAll() {
        insulinDao.deleteAll()
    }
}