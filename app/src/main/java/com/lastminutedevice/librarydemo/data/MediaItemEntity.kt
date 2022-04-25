package com.lastminutedevice.librarydemo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lastminutedevice.librarydemo.MediaType

@Entity(tableName = "media_items")
data class MediaItemEntity(
    val title: String,
    val type: MediaType
) {
    @PrimaryKey(autoGenerate = true) var id: Int? = null
}
