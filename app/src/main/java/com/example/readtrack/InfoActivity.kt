package com.example.readtrack

import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.readtrack.data.Book
import com.example.readtrack.data.BookViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.Integer.parseInt

class InfoActivity : AppCompatActivity() {
    lateinit var create_button: View
    private lateinit var mBookViewModel: BookViewModel
    lateinit var delete_button: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        mBookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)

        if (MainActivity.MySingleton.book_updated != 0) {
            mBookViewModel.readAllData.observe(this, Observer {
                try {
                    MainActivity.MySingleton.updatingbook =
                        it[MainActivity.MySingleton.book_updated - 1]
                    MainActivity.MySingleton.updatingbookid =
                        it[MainActivity.MySingleton.book_updated - 1].id
                    MainActivity.MySingleton.updatingbooktitle =
                        it[MainActivity.MySingleton.book_updated - 1].title
                    MainActivity.MySingleton.updatingbookstart =
                        it[MainActivity.MySingleton.book_updated - 1].start
                    MainActivity.MySingleton.updatingbooktotal =
                        it[MainActivity.MySingleton.book_updated - 1].total
                    MainActivity.MySingleton.updatingbookdays =
                        it[MainActivity.MySingleton.book_updated - 1].days
                    MainActivity.MySingleton.updatingbookcreated =
                        it[MainActivity.MySingleton.book_updated - 1].created

                    findViewById<EditText>(R.id.book_name_edit).setText(MainActivity.MySingleton.updatingbooktitle)
                    findViewById<EditText>(R.id.start_page_edit).setText(MainActivity.MySingleton.updatingbookstart.toString())
                    findViewById<EditText>(R.id.total_page_edit).setText(MainActivity.MySingleton.updatingbooktotal.toString())
                    findViewById<EditText>(R.id.days_edit).setText(MainActivity.MySingleton.updatingbookdays.toString())

                    findViewById<TextView>(R.id.textView).setText("Update Book")
                    findViewById<Button>(R.id.create_button).setText("Update")
                } catch (t: Throwable) {

                }
            })
        }

        //set delete button functionality
        delete_button = findViewById(R.id.delete_button)
        delete_button.setOnClickListener { delete_book() }
        MainActivity.MySingleton.book_deleted = MainActivity.MySingleton.book_updated

        //check for create button clicked
        create_button = findViewById<Button>(R.id.create_button)
        create_button.setOnClickListener {
            if (MainActivity.MySingleton.book_updated == 0) {
                //increase the book count
                MainActivity.MySingleton.num_books++

                //assign book that is created
                when (MainActivity.MySingleton.num_books) {
                    1 -> MainActivity.MySingleton.book1_created = true
                    2 -> MainActivity.MySingleton.book2_created = true
                    3 -> MainActivity.MySingleton.book3_created = true
                    4 -> MainActivity.MySingleton.book4_created = true
                    5 -> MainActivity.MySingleton.book5_created = true
                }

                //add data to database
                insertDataToDatabase()

                //switch back to main activity
                val intent = Intent(this, MainActivity::class.java).apply {}
                startActivity(intent)
            } else {
                //assign book that is created
                when (MainActivity.MySingleton.book_updated) {
                    1 -> MainActivity.MySingleton.book1_created = true
                    2 -> MainActivity.MySingleton.book2_created = true
                    3 -> MainActivity.MySingleton.book3_created = true
                    4 -> MainActivity.MySingleton.book4_created = true
                    5 -> MainActivity.MySingleton.book5_created = true
                }

                //update data in database
                updateDataToDatabase()

                //switch back to main activity
                val intent = Intent(this, MainActivity::class.java).apply {}
                startActivity(intent)
            }
        }
    }

    private fun insertDataToDatabase() {
        val title = findViewById<EditText>(R.id.book_name_edit).text.toString()
        val start = findViewById<EditText>(R.id.start_page_edit).text
        val total = findViewById<EditText>(R.id.total_page_edit).text
        val days = findViewById<EditText>(R.id.days_edit).text

        if (inputCheck(title, start, total, days)){
            //create book object
            val book = Book(
                MainActivity.MySingleton.num_books,
                title,
                parseInt(start.toString()),
                parseInt(total.toString()),
                parseInt(days.toString()),
                true)

            //get values for notification
            MainActivity.MySingleton.current_title = book.title
            MainActivity.MySingleton.current_start = book.start
            MainActivity.MySingleton.current_total = book.total
            MainActivity.MySingleton.current_days = book.days

            //add data to database
            mBookViewModel.addBook(book)
        } else {
            Toast.makeText(this@InfoActivity, "Please fill out all fields!", Toast.LENGTH_SHORT).show()

            //unassign book that is no longer created
            when (MainActivity.MySingleton.num_books) {
                1 -> MainActivity.MySingleton.book1_created = false
                2 -> MainActivity.MySingleton.book2_created = false
                3 -> MainActivity.MySingleton.book3_created = false
                4 -> MainActivity.MySingleton.book4_created = false
                5 -> MainActivity.MySingleton.book5_created = false
            }
        }
    }

    private fun updateDataToDatabase() {
        val title= findViewById<EditText>(R.id.book_name_edit).text.toString()
        val start = findViewById<EditText>(R.id.start_page_edit).text
        val total = findViewById<EditText>(R.id.total_page_edit).text
        val days = findViewById<EditText>(R.id.days_edit).text

        if (inputCheck(title, start, total, days)) {
            //create book object
            val book = Book(
                MainActivity.MySingleton.book_updated,
                title,
                parseInt(start.toString()),
                parseInt(total.toString()),
                parseInt(days.toString()),
                true
            )

            //get values for notification
            MainActivity.MySingleton.current_title = book.title
            MainActivity.MySingleton.current_start = book.start
            MainActivity.MySingleton.current_total = book.total
            MainActivity.MySingleton.current_days = book.days

            //delete previous notification
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel((101+MainActivity.MySingleton.book_updated))

            //update book
            mBookViewModel.updateBook(book)
        } else {
            //fail message
            Toast.makeText(this@InfoActivity, "Please fill out all fields!", Toast.LENGTH_SHORT).show()

            //unassign book that is no longer created
            when (MainActivity.MySingleton.book_updated) {
                1 -> MainActivity.MySingleton.book1_created = false
                2 -> MainActivity.MySingleton.book2_created = false
                3 -> MainActivity.MySingleton.book3_created = false
                4 -> MainActivity.MySingleton.book4_created = false
                5 -> MainActivity.MySingleton.book5_created = false
            }
            MainActivity.MySingleton.book_updated = 0
        }
    }

    private fun delete_book() {
        if (MainActivity.MySingleton.num_books > MainActivity.MySingleton.book_updated){
            Toast.makeText(this@InfoActivity, "This book cannot be deleted. Later books must be deleted first.", Toast.LENGTH_SHORT).show()

            //switch back to main activity
            val intent = Intent(this, MainActivity::class.java).apply {}
            startActivity(intent)
        } else if (MainActivity.MySingleton.book_updated != 0) {
            MainActivity.MySingleton.book_updated = 0

            //check for deletion confirmation
            val builder = AlertDialog.Builder(this@InfoActivity)
            builder.setPositiveButton("Yes"){_,_->
                val book = Book(
                    MainActivity.MySingleton.updatingbookid,
                    MainActivity.MySingleton.updatingbooktitle,
                    MainActivity.MySingleton.updatingbookstart,
                    MainActivity.MySingleton.updatingbooktotal,
                    MainActivity.MySingleton.updatingbookdays,
                    MainActivity.MySingleton.updatingbookcreated
                )

                //unassign book that is no longer created
                when (MainActivity.MySingleton.book_deleted) {
                    1 -> {
                        MainActivity.MySingleton.book1_created = false
                    }
                    2 -> {
                        MainActivity.MySingleton.book2_created = false
                    }
                    3 -> {
                        MainActivity.MySingleton.book3_created = false
                    }
                    4 -> {
                        MainActivity.MySingleton.book4_created = false
                    }
                    5 -> {
                        MainActivity.MySingleton.book5_created = false
                    }
                }

                MainActivity.MySingleton.num_books -= 1

                mBookViewModel.deleteBook(book)
                Toast.makeText(this@InfoActivity, "Book successfully deleted", Toast.LENGTH_SHORT).show()

                //switch back to main activity
                val intent = Intent(this, MainActivity::class.java).apply {}
                startActivity(intent)
            }
            builder.setNegativeButton("No"){_,_->}
            builder.setTitle("Delete " + MainActivity.MySingleton.updatingbooktitle + "?")
            builder.setMessage("Are you sure you want to delete this book?")
            builder.create().show()
        } else {
            //switch back to main activity
            val intent = Intent(this, MainActivity::class.java).apply {}
            startActivity(intent)
        }
    }

    private fun inputCheck(title: String, start: Editable, total: Editable, days: Editable):Boolean {
        return !(TextUtils.isEmpty(title) || start.isEmpty() || total.isEmpty() || days.isEmpty())
    }
}