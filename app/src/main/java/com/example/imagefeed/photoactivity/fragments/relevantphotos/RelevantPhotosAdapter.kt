package com.example.imagefeed.photoactivity.fragments.relevantphotos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.imagefeed.R
import com.squareup.picasso.Picasso

class RelevantPhotosAdapter(val photoUrls: List<String>): RecyclerView.Adapter<RelevantPhotosAdapter.RelevantPhotosViewHolder>() {
    class RelevantPhotosViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val photo: ImageView = itemView.findViewById(R.id.relevantPhoto)

        fun bind(url: String) {
            Picasso.get().isLoggingEnabled=true
            Picasso.get()
                .load(url)
                .error(R.drawable.failure_picture)
                .into(photo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelevantPhotosViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_relevant_photo, parent, false)

        return RelevantPhotosViewHolder(view)
    }

    override fun getItemCount(): Int {
        return photoUrls.size
    }

    override fun onBindViewHolder(holder: RelevantPhotosViewHolder, position: Int) {
        holder.bind(photoUrls[position])
    }
}