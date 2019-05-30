package com.example.imagefeed.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.imagefeed.R
import com.example.imagefeed.model.ImageRecord
import com.squareup.picasso.Picasso

class MainAdapter(private val records: MutableList<ImageRecord>): RecyclerView.Adapter<MainAdapter.RecordViewHolder>() {

    class RecordViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.singleElemImage)
        private val name: TextView = itemView.findViewById(R.id.singleElemNameValue)
        private val date: TextView = itemView.findViewById(R.id.singleElemDateValue)
        private val tags: TextView = itemView.findViewById(R.id.singleElemTagsValue)

        fun bind(record: ImageRecord) {
            Picasso.get().isLoggingEnabled=true
            Picasso.get()
                .load(record.url)
                .error(R.drawable.failure_picture)
                .into(image)

            name.text = record.name
            date.text = ImageRecord.DATE_FORMATTER.format(record.date)
            tags.text = record.tags.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_element, parent, false)

        return RecordViewHolder(view)
    }

    override fun getItemCount(): Int {
        return records.size
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.bind(records[position])
    }

    fun removeRecord(viewHolder: RecyclerView.ViewHolder){
        records.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)
    }

    fun addRecord(record: ImageRecord){
        records.add(0, record)
        notifyItemInserted(0)
    }
}