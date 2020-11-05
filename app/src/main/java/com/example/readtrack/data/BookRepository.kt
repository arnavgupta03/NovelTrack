package com.example.readtrack.data

import androidx.lifecycle.LiveData

class BookRepository(private val bookDao: BookDao) {

    val readAllData: LiveData<List<Book>> = bookDao.readAllData()

    suspend fun addBook(book: Book) {
        bookDao.addBook(book)
    }

    suspend fun updateBook(book: Book) {
        bookDao.updateBook(book)
    }

    suspend fun deleteBook(book: Book) {
        bookDao.deleteBook(book)
    }

    fun hasItem(id: Int): Boolean {
        return bookDao.hasItem(id)
    }

    fun isExists(): Boolean {
        return bookDao.isExists()
    }

    suspend fun updateId(id: Int, new_id: Int) {
        bookDao.updateId(id, new_id)
    }
}