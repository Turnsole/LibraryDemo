package com.lastminutedevice.librarydemo.data

import androidx.room.TypeConverter
import com.lastminutedevice.librarydemo.MediaType

class MediaTypeConverter {

    @TypeConverter
    fun fromMediaType(typeName: String) : MediaType {
        return MediaType.valueOf(typeName.uppercase())
    }

    @TypeConverter
    fun toMediaType(mediaType: MediaType) : String {
        return mediaType.typeName
    }
}
