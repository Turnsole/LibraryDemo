package com.lastminutedevice.librarydemo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rentals")
data class RentalEntity(
    val mediaId: Int,
    val userId: Int
) {
    @PrimaryKey(autoGenerate = true) var id: Int? = null
}
