package com.lastminutedevice.librarydemo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [MediaItemEntity::class, MediaTypeEntity::class, UserEntity::class, RentalEntity::class],
    version = 1
)
@TypeConverters(MediaTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): Dao
}
