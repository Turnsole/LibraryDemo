package com.lastminutedevice.librarydemo.data

import com.lastminutedevice.librarydemo.MediaType
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.random.Random

class DataGenerator(private val dao: Dao, private val userId: Int) {

    private val random = Random(1)

    private val seedWords = listOf(
        "apple", "dirt", "brick", "ship", "storm", "softly", "waving", "deciduous"
    )

    fun fillDatabase() {
        generateTypes()
            .andThen(generateItems())
            .andThen(generateUser())
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                // Both the items and the user must exist before rentals can.
                generateRentals()
            }
    }

    /**
     * Add each of the media types.
     */
    private fun generateTypes(): Completable {
        return dao.addMediaTypes(MediaType.values().asList().map { MediaTypeEntity(type = it, consumable = false) })
    }

    /**
     * Add some randomly generated media items.
     */
    private fun generateItems(): Completable {
        val items = mutableListOf<MediaItemEntity>()
        for (i in 1..50) {
            val newItem = MediaItemEntity(
                title = generateTitle(),
                type = MediaType.values().random()
            )
            items.add(newItem)
        }
        return dao.addMediaItems(items)
    }

    /**
     * Add a user.
     */
    private fun generateUser(): Completable {
        val user = UserEntity(name = "Bob Ross", userId = userId)
        return dao.addUser(user).subscribeOn(Schedulers.io())
    }

    /**
     * Add some randomly generated rentals.
     */
    private fun generateRentals() {
        dao.getAllMediaItems()
            .subscribe { list ->
                val listSize = list.size
                val rentalList = mutableListOf<RentalEntity>()
                // Generate a specific number of rentals, unevenly distributed among media items.
                for (i in 0..50) {
                    val itemId = list[random.nextInt(listSize)].id!!
                    val rental = RentalEntity(
                        mediaId = itemId,
                        userId = if (random.nextBoolean()) userId else userId + 1
                    )
                    rentalList.add(rental)
                }
                dao.addRentals(rentalList).subscribe()
            }
    }

    /**
     * Randomly choose between 1 and 3 words from the list to form a title.
     */
    private fun generateTitle(): String {
        val result = mutableListOf<String>()
        for (i in 0..random.nextInt(1, 3)) {
            result.add(seedWords.random())
        }
        return result.joinToString(" ")
    }
}
