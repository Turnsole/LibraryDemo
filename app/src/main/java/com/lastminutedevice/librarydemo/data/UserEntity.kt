package com.lastminutedevice.librarydemo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val userId: Int,
    val name: String
)
