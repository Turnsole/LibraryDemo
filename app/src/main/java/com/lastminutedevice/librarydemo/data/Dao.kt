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

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addEditions(rentals: List<EditionEntity>): Completable

    /**
     * Used by the generator, deals directly with the media item entity.
     */
    @Query("select * from media_items")
    fun getAllMediaItems(): Single<List<MediaItemEntity>>

    @Query("select * from media_items left join editions on media_items.mediaItemId = editions.editionMediaId left join rentals on media_items.mediaItemId = rentals.rentalMediaId where media_items.type = :mediaType order by media_items.mediaItemId")
    fun getAllItemsOfType(mediaType: MediaType): Single<Map<MediaItemEntity, List<MediaRelation>>>

    @Transaction
    @Query("select * from media_items left join editions on media_items.mediaItemId = editions.editionMediaId left join rentals on media_items.mediaItemId = rentals.rentalMediaId where media_items.type = :mediaType and userId = :userId order by media_items.mediaItemId")
    fun getUsersRentals(
        mediaType: MediaType,
        userId: Int
    ): Single<Map<MediaItemEntity, List<MediaRelation>>>
}
