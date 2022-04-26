package com.lastminutedevice.librarydemo.adapter

import com.lastminutedevice.librarydemo.data.MediaItemEntity
import com.lastminutedevice.librarydemo.data.RentalEntity

data class MediaItemData(

    val mediaItem: MediaItemEntity,

    val rentals: List<RentalEntity>
)