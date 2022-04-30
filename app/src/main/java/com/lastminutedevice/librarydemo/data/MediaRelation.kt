package com.lastminutedevice.librarydemo.data

import androidx.room.Embedded
import androidx.room.Relation

class MediaRelation(
    @Embedded val mediaItemEntity: MediaItemEntity,
    @Relation(
        parentColumn = "mediaItemId",
        entityColumn = "rentalMediaId"
    )
    val rentals: List<RentalEntity>,
    @Relation(
        parentColumn = "mediaItemId",
        entityColumn = "editionMediaId"
    )
    val editions: List<EditionEntity>
)
