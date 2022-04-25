package com.lastminutedevice.librarydemo.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lastminutedevice.librarydemo.R

class MediaItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val titleView: TextView = view.findViewById(R.id.title)

    val detailsView: TextView = view.findViewById(R.id.details)
}
