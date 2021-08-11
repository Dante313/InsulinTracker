package com.dante.android.insulintracker.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dante.android.insulintracker.model.Insulin

@Dao
interface InsulinDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addInsulin(insulin: Insulin)

    @Update
    suspend fun updateInsulin(insulin: Insulin)

    @Delete
    suspend fun deleteInsulin(insulin: Insulin)

    @Query("DELETE FROM insulin_table")
    suspend fun deleteAll()


    @Query("SELECT * FROM insulin_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Insulin>>
}