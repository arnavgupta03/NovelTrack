package com.example.readtrack.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookViewModel(application: Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<Book>>
    private val repository: BookRepository

    init {
        val bookDao = BookDatabase.getDatabase(application).bookDao()
        repository = BookRepository(bookDao)
        readAllData = repository.readAllData
    }

    fun addBook(book: Book){
        viewModelScope.launch(Dispatchers.IO){
            repository.addBook(book)
        }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateBook(book)
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteBook(book)
        }
    }

    fun updateId(id: Int, new_id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateId(id, new_id)
        }
    }

    fun hasItem(id: Int):Boolean {
        viewModelScope.launch {

        }
        return true
    }

    fun isExists(): Boolean {
        viewModelScope.launch {

        }
        return true
    }
}