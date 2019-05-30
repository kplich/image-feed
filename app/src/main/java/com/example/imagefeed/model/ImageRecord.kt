package com.example.imagefeed.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
class ImageRecord(val url: String, val name: String, val date: Date, val tags: List<String>) : Parcelable {
    companion object {
        val DATE_FORMATTER = SimpleDateFormat("dd.MM.yyyy")
    }

    fun numberOfTagsInCommon(first: ImageRecord, second: ImageRecord): Int {
        var count = 0
        for (tag: String in first.tags) {
            if(second.tags.contains(tag)) {
                count++
            }
        }

        return count
    }
}