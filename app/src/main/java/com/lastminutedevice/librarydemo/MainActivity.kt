package com.lastminutedevice.librarydemo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.lastminutedevice.librarydemo.adapter.ViewAdapter
import com.lastminutedevice.librarydemo.data.AppDatabase
import com.lastminutedevice.librarydemo.data.DataGenerator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private val pretendUserId = 12234

    private val adapter = ViewAdapter(this)

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = Room
            .inMemoryDatabaseBuilder(applicationContext, AppDatabase::class.java)
            .build()

        // Fill the db with random data.
        DataGenerator(dao = database.dao(), userId = pretendUserId).fillDatabase()
    }

    override fun onResume() {
        super.onResume()

        // Set an adapter on the RecyclerView.
        findViewById<RecyclerView>(R.id.list).adapter = adapter

        // Set a listener on the checkbox to toggle between a user search and a general search.
        findViewById<AppCompatCheckBox>(R.id.checkbox).setOnCheckedChangeListener { _, userOnly ->
            loadList(userOnly)
        }

        // Do an initial list load.
        loadList(false)
    }

    /**
     * TODO MediaType toggle
     */
    private fun loadList(userOnly: Boolean, mediaType: MediaType = MediaType.BOOK) {
        val observable = if (userOnly) {
            database.dao().getUsersRentals(mediaType = mediaType, userId = pretendUserId)
        } else {
            database.dao().getAllItemsOfType(mediaType = mediaType)
        }
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { items ->
                findViewById<TextView>(R.id.search_summary).run {
                    text = resources.getString(R.string.search_summary, items.size, mediaType.typeName)
                }
                adapter.resetData(newData = items)
            }
    }
}
