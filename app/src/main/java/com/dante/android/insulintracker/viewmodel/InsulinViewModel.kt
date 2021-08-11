package com.dante.android.insulintracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.dante.android.insulintracker.data.InsulinDatabase
import com.dante.android.insulintracker.model.Insulin
import com.dante.android.insulintracker.repository.InsulinRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InsulinViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Insulin>>
    private val repository: InsulinRepository

    init {
        val insulinDao = InsulinDatabase.getDatabase(application).insulinDao()
        repository = InsulinRepository(insulinDao)
        readAllData = repository.readAllData
    }

    fun addInsulin(insulin: Insulin) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addInsulin(insulin)
        }
    }

    fun updateInsulin(insulin: Insulin) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateInsulin(insulin)
        }
    }

    fun deleteInsulin(insulin: Insulin) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteInsulin(insulin)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }
}