package com.dante.android.insulintracker.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "insulin_table")
data class Insulin(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val fullness: Int): Parcelable