package com.lastminutedevice.librarydemo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "editions")
data class EditionEntity(val editionMediaId: Int) {
    @PrimaryKey(autoGenerate = true) var editionId: Int? = null
}
