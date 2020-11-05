package com.example.readtrack

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.readtrack.data.Book
import com.example.readtrack.data.BookViewModel
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    //create global
    private lateinit var add_button: View

    //find database
    private lateinit var mBookViewModel: BookViewModel

    //create channel constants
    private var CHANNEL_ID = "channel_id_readtrack"
    private val notificationId = 101

    object MySingleton {
        var num_books: Int = 0
        var book1_created: Boolean = false
        var book2_created: Boolean = false
        var book3_created: Boolean = false
        var book4_created: Boolean = false
        var book5_created: Boolean = false
        lateinit var book: Button
        var current_title: String = ""
        var current_start: Int = 0
        var current_total: Int = 0
        var current_days: Int = 0
        var book_updated: Int = 0
        var book_deleted: Int = 0
        lateinit var updatingbook: Book
        var updatingbookid: Int = 0
        var updatingbooktitle: String = ""
        var updatingbookstart: Int = 0
        var updatingbooktotal: Int = 0
        var updatingbookdays: Int = 0
        var updatingbookcreated: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //allow for button functionality
        add_button = findViewById(R.id.add_book)
        add_button.setOnClickListener {createBook(MySingleton.num_books)}

        findViewById<Button>(R.id.book_1).setOnClickListener {updateBook(1)}
        findViewById<Button>(R.id.book_2).setOnClickListener {updateBook(2)}
        findViewById<Button>(R.id.book_3).setOnClickListener {updateBook(3)}
        findViewById<Button>(R.id.book_4).setOnClickListener {updateBook(4)}
        findViewById<Button>(R.id.book_5).setOnClickListener {updateBook(5)}

        //setup database view
        mBookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)

        mBookViewModel.readAllData.observe(this, Observer {
            //check if book 1 is created
            try {
                if (it[0].created) {
                    try {
                        if (it[1].created) {
                            try {
                                if (it[2].created) {
                                    try {
                                        if (it[3].created) {
                                            try {
                                                if (it[4].created) {
                                                    //set up book button
                                                    setUpBook5()
                                                }
                                            } catch (t: Throwable) {

                                            }
                                            //set up book button
                                            setUpBook4()
                                        }
                                    } catch (t: Throwable) {

                                    }
                                    //set up book button
                                    setUpBook3()
                                }
                            } catch (t: Throwable) {

                            }
                            //set up book button
                            setUpBook2()
                        }
                    } catch (t: Throwable) {

                    }
                    //set up book button
                    setUpBook1()
                }
            }
            catch (t: Throwable) {
                Toast.makeText(this@MainActivity, "Make your first book!", Toast.LENGTH_SHORT).show()
            }
        })

        //check which book is newly created
        if (MySingleton.book1_created) {
            //turn off book created
            MySingleton.book1_created = false

            //create notification
            createBookNotification(1)
        } else if (MySingleton.book2_created) {
            //turn off book created
            MySingleton.book2_created = false

            //create notification
            createBookNotification(2)
        } else if (MySingleton.book3_created) {
            //turn off book created
            MySingleton.book3_created = false

            //create notification
            createBookNotification(3)
        } else if (MySingleton.book4_created) {
            //turn off book created
            MySingleton.book4_created = false

            //create notification
            createBookNotification(4)
        } else if (MySingleton.book5_created) {
            //turn off book created
            MySingleton.book5_created = false

            //create notification
            createBookNotification(5)
        }
    }

    private fun createBook(book_num: Int) {
        //check that total possible books are not exceeded
        if (book_num < 5) {
            //create intent for use
            val intent = Intent(this, InfoActivity::class.java).apply{}
            //start info activity
            startActivity(intent)
        } else {
            Toast.makeText(this@MainActivity, "Only 5 books allowed!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createBookNotification(num: Int) {
        createNotificationChannel()
        
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle(MySingleton.current_title)
            .setContentText((ceil((MySingleton.current_total.toDouble() - MySingleton.current_start.toDouble())/(MySingleton.current_days.toDouble())).toInt()).toString() + " pages left to read for today!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)

        with (NotificationManagerCompat.from(this)) {
            notify((notificationId + num), builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ReadTrack"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setUpBook1() {
        val book: Button = findViewById(R.id.book_1)

        //get book title
        mBookViewModel.readAllData.observe(this, Observer {
            book.text = it[0].title
        })

        //change button visibility
        book.visibility = View.VISIBLE

        //update book count
        if (MySingleton.num_books <= 1){
            MySingleton.num_books = 1
        }

        //set no book updated
        MySingleton.book_updated = 0
    }

    private fun setUpBook2() {
        val book: Button = findViewById(R.id.book_2)
        //val progBar: ProgressBar = findViewById(R.id.progBarBook2)

        //get book title
        mBookViewModel.readAllData.observe(this, Observer {
            book.text = it[1].title
            //progBar.progress = floor((it[1].total.toDouble() - it[1].start.toDouble())/(it[1].days)).toInt()
        })

        //change button visibility
        book.visibility = View.VISIBLE
        //progBar.visibility = View.VISIBLE

        //update book count
        if (MySingleton.num_books <= 2){
            MySingleton.num_books = 2
        }

        //set no book updated
        MySingleton.book_updated = 0
    }

    private fun setUpBook3() {
        val book: Button = findViewById(R.id.book_3)
        //val progBar: ProgressBar = findViewById(R.id.progBarBook3)

        //get book title
        mBookViewModel.readAllData.observe(this, Observer {
            book.text = it[2].title
            //progBar.progress = floor((it[2].total.toDouble() - it[2].start.toDouble())/(it[2].days)).toInt()
        })

        //change button visibility
        book.visibility = View.VISIBLE
        //progBar.visibility = View.VISIBLE

        //update book count
        if (MySingleton.num_books <= 3){
            MySingleton.num_books = 3
        }

        //set no book updated
        MySingleton.book_updated = 0
    }

    private fun setUpBook4() {
        val book: Button = findViewById(R.id.book_4)
        //val progBar: ProgressBar = findViewById(R.id.progBarBook4)
        //get book title
        mBookViewModel.readAllData.observe(this, Observer {
            book.text = it[3].title
            //progBar.progress = floor((it[3].total.toDouble() - it[3].start.toDouble())/(it[3].days)).toInt()
        })

        //change button visibility
        book.visibility = View.VISIBLE
        //progBar.visibility = View.VISIBLE

        //update book count
        if (MySingleton.num_books <= 4){
            MySingleton.num_books = 4
        }

        //set no book updated
        MySingleton.book_updated = 0
    }

    private fun setUpBook5() {
        val book: Button = findViewById(R.id.book_5)
        //val progBar: ProgressBar = findViewById(R.id.progBarBook5)

        //get book title
        mBookViewModel.readAllData.observe(this, Observer {
            book.text = it[4].title
            //progBar.progress = floor((it[4].total.toDouble() - it[4].start.toDouble())/(it[4].days)).toInt()
        })

        //change button visibility
        book.visibility = View.VISIBLE
        //progBar.visibility = View.VISIBLE

        //update book count
        if (MySingleton.num_books <= 5){
            MySingleton.num_books = 5
        }

        //set no book updated
        MySingleton.book_updated = 0
    }

    private fun updateBook (num: Int) {
        MySingleton.book_updated = num

        //create intent for use
        val intent = Intent(this, InfoActivity::class.java).apply{
        }
        //start info activity
        startActivity(intent)
    }
}