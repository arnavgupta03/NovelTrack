package com.example.readtrack.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBook(book: Book)

    @Query("SELECT * FROM book_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Book>>

    @Query("SELECT EXISTS(SELECT * FROM book_table WHERE id = :id)")
    fun hasItem(id:Int): Boolean

    @Query("SELECT EXISTS(SELECT * FROM book_table)")
    fun isExists(): Boolean

    @Query("UPDATE book_table SET id=:new_id WHERE id=:id")
    fun updateId(id: Int, new_id: Int)

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)
}