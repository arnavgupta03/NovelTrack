package com.example.readtrack.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "book_table")
data class Book(
    @PrimaryKey
    var id: Int,
    var title: String,
    var start: Int,
    var total: Int,
    var days: Int,
    var created: Boolean
): Parcelable