package com.lastminutedevice.librarydemo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lastminutedevice.librarydemo.R
import com.lastminutedevice.librarydemo.data.MediaItemData

/**
 * Adapter for all the items in the big list.
 */
class MediaItemAdapter(private val context: Context) : RecyclerView.Adapter<MediaItemViewHolder>() {

    private val data: MutableList<MediaItemData> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged") // Values only change when search changes.
    fun resetData(newData: List<MediaItemData>) {
        data.clear()
        data.addAll(newData)
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
