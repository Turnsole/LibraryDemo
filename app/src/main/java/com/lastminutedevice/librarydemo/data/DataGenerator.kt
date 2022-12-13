package com.lastminutedevice.librarydemo.data

import com.lastminutedevice.librarydemo.MediaType
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.random.Random

class DataGenerator(private val dao: Dao, private val userId: Int) {

    private val random = Random(System.currentTimeMillis())

    private val titles = ArrayDeque<String>()

    private val maxNumberItems = 25

    fun fillDatabase() {

        generateTitles()

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
        for (i in 1..maxNumberItems) {
            val newItem = MediaItemEntity(
                title = titles.removeFirst(),
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
     * Add some randomly generated rentals and editions.
     */
    private fun generateRentals() {
        dao.getAllMediaItems()
            .subscribe { list ->
                val listSize = list.size

                // Generate a specific number of rentals, unevenly distributed among media items.
                val rentalList = mutableListOf<RentalEntity>()
                for (i in 0..maxNumberItems) {
                    val itemId = list[random.nextInt(listSize)].mediaItemId!!
                    val rental = RentalEntity(
                        rentalMediaId = itemId,
                        userId = if (random.nextBoolean()) userId else userId + 1
                    )
                    rentalList.add(rental)
                }
                dao.addRentals(rentalList).subscribe()

                // Generate some arbitrary editions for a random distribution of media items.
                val editionList = mutableListOf<EditionEntity>()
                for (j in 0..maxNumberItems) {
                    val itemId = list[random.nextInt(listSize)].mediaItemId!!
                    val edition = EditionEntity(editionMediaId = itemId)
                    editionList.add(edition)
                }
                dao.addEditions(editionList).subscribe()
            }
    }

    /**
     * Randomly choose between 1 and 3 words from the list to form a unique title, which will
     * be added to the deque for use for each media type.
     */
    private fun generateTitles()  {
        val titleSet = mutableSetOf<String>()
        val seedWords = listOf("Apple", "Dirt", "Brick", "Ship", "Storm", "Softly", "Waving", "Deciduous", "Sky", "Moss", "Yule")
        while (titleSet.size <= maxNumberItems) {
            val result = mutableSetOf<String>()
            for (i in 0..random.nextInt(0, 4)) {
                result.add(seedWords.random())
            }
            titleSet.add(result.joinToString(" "))
        }
        titles.addAll(titleSet)
    }
}
