package com.lastminutedevice.librarydemo.data

import androidx.room.Embedded
import androidx.room.Relation

data class MediaItemData(
    @Embedded
    val mediaItem: MediaItemEntity,

    @Relation(parentColumn = "id", entityColumn = "mediaId")
    val rentals: List<RentalEntity>
)
