package com.lastminutedevice.librarydemo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lastminutedevice.librarydemo.R
import com.lastminutedevice.librarydemo.data.MediaItemEntity
import com.lastminutedevice.librarydemo.data.RentalEntity

/**
 * Adapter for all the items in the big list.
 */
class MediaItemAdapter(private val context: Context) : RecyclerView.Adapter<MediaItemViewHolder>() {

    private val data: MutableList<MediaItemData> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged") // Values only change when search changes.
    fun resetData(newData: Map<MediaItemEntity, List<RentalEntity>>) {
        data.clear()

        // The adapter is a ListAdapter, so transform the results.
        val list = newData.map { entry -> MediaItemData(entry.key, entry.value) }
        data.addAll(list)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemViewHolder {
        return MediaItemViewHolder(
            view = LayoutInflater
                .from(context)
                .inflate(R.layout.media_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MediaItemViewHolder, position: Int) {
        val itemData = data[position]
        holder.titleView.text = itemData.mediaItem.title
        holder.detailsView.run {
            text = resources.getString(
                R.string.media_item_details,
                itemData.mediaItem.mediaItemId,
                itemData.mediaItem.type,
                itemData.rentals.size)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
