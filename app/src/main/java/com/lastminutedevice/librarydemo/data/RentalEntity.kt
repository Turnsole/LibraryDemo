package com.lastminutedevice.librarydemo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rentals")
data class RentalEntity(
    val rentalMediaId: Int,
    val userId: Int
) {
    @PrimaryKey(autoGenerate = true) var rentalId: Int? = null
}
