package com.ahmetardakavakci.benote.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "title") val titleString: String?,
    @ColumnInfo(name = "content") val contentString: String?,
    @ColumnInfo(name = "color") val colorInt: Int?
)
