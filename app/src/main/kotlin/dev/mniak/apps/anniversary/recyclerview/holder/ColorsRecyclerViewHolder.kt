package dev.mniak.apps.anniversary.recyclerview.holder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import dev.mniak.apps.anniversary.R
import dev.mniak.apps.anniversary.recyclerview.adapter.ColorsRecyclerViewAdapter

class ColorsRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val colorItem : ImageView = itemView.findViewById(R.id.color_item)

    fun bind(item: Int, listener: ColorsRecyclerViewAdapter.RecyclerViewOnItemClickListener) {
        itemView.setOnClickListener { listener.onItemClick(item) }
    }
}
