package com.lastminutedevice.librarydemo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lastminutedevice.librarydemo.MediaType

@Entity(tableName = "media_types")
data class MediaTypeEntity (
    @PrimaryKey val type: MediaType,
    val consumable: Boolean
)
