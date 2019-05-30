package com.example.imagefeed.photoactivity.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.example.imagefeed.R
import com.example.imagefeed.model.ImageRecord
import com.squareup.picasso.Picasso

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val IMAGE_RECORD = "IMAGE_RECORD"

class PhotoOnlyFragment : Fragment() {

    // parameters
    private lateinit var chosenRecord: ImageRecord

    //widgets
    private var chosenRecordPhoto: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chosenRecord = it.getParcelable(IMAGE_RECORD)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_only, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chosenRecordPhoto = view.findViewById(R.id.chosenRecordPhoto)

        Picasso.get()
            .load(chosenRecord.url)
            .into(chosenRecordPhoto)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param chosenRecord Parameter 1.
         * @return A new instance of fragment PhotoOnlyFragment.
         */
        @JvmStatic
        fun newInstance(chosenRecord: ImageRecord) =
            PhotoOnlyFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(IMAGE_RECORD, chosenRecord)
                }
            }
    }
}
