package com.lastminutedevice.librarydemo

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.lastminutedevice.librarydemo.adapter.MediaItemAdapter
import com.lastminutedevice.librarydemo.data.AppDatabase
import com.lastminutedevice.librarydemo.data.DataGenerator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private val pretendUserId = 123

    private val mediaItemAdapter = MediaItemAdapter(this)

    private lateinit var mediaTypeAdapter: ArrayAdapter<CharSequence>

    private lateinit var database: AppDatabase

    private var mediaType = MediaType.BOOK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mediaTypeAdapter = ArrayAdapter.createFromResource(
            this@MainActivity,
            R.array.media_type_resource,
            android.R.layout.simple_spinner_item
        )

        findViewById<Spinner>(R.id.dropdown_menu).run {
            adapter = mediaTypeAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val value = (parent?.getItemAtPosition(position) as String).uppercase()
                    mediaType = MediaType.valueOf(value)
                    loadList()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) { /* no-op */}
            }
        }

        // Create an in-memory database so the state is clean every time the app starts.
        database = Room
            .inMemoryDatabaseBuilder(applicationContext, AppDatabase::class.java)
            .build()

        // Fill the db with random data.
        DataGenerator(dao = database.dao(), userId = pretendUserId).fillDatabase()
    }

    override fun onResume() {
        super.onResume()

        // Set an adapter on the RecyclerView.
        findViewById<RecyclerView>(R.id.list).adapter = mediaItemAdapter

        // Set a listener on the checkbox to toggle between a user search and a general search.
        findViewById<AppCompatCheckBox>(R.id.checkbox).setOnCheckedChangeListener { _, _ ->
            loadList()
        }

        // Do an initial list load.
        loadList()
    }

    private fun loadList() {
        val userOnly = findViewById<AppCompatCheckBox>(R.id.checkbox).isChecked

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
                    text = resources.getString(R.string.search_summary, items.values.size, mediaType.typeName)
                }
                mediaItemAdapter.resetData(newData = items)
            }
    }
}
