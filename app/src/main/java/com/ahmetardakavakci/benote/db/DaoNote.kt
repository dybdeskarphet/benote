package com.ahmetardakavakci.benote.db

import androidx.room.*

@Dao
interface DaoNote {

    @Query("SELECT * FROM notes")
    fun getAll(): List<Note>

    @Query("SELECT * FROM notes Where id = :id")
    suspend fun getById(id: Int): Note

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("UPDATE notes SET title=:title, content=:content, color=:color WHERE id=:id")
    suspend fun update(id: Int, title: String, content: String, color: Int)

}