package com.lastminutedevice.librarydemo.data

import androidx.room.*
import androidx.room.Dao
import com.lastminutedevice.librarydemo.MediaType
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(type: UserEntity): Completable

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMediaTypes(types: List<MediaTypeEntity>): Completable

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMediaItems(mediaItems: List<MediaItemEntity>): Completable

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRentals(rentals: List<RentalEntity>): Completable

    /**
     * Used by the generator, deals directly with the media item entity.
     */
    @Query("select * from media_items")
    fun getAllMediaItems(): Single<List<MediaItemEntity>>

    @Query("select * from media_items where type = :mediaType")
    fun getAllItemsOfType(mediaType: MediaType): Single<List<MediaItemData>>

    @Transaction
    @Query("select * from media_items left join rentals on media_items.id = mediaId where type = :mediaType and userId = :userId")
    fun getUsersRentals(mediaType: MediaType, userId: Int): Single<List<MediaItemData>>
}
