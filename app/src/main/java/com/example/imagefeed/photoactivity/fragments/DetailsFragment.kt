package com.example.imagefeed.photoactivity.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.imagefeed.R
import com.example.imagefeed.model.ImageRecord

private const val CHOSEN_RECORD = "CHOSEN_RECORD"

class DetailsFragment : Fragment() {
    // parameters
    private lateinit var chosenRecord: ImageRecord

    //widgets
    private lateinit var name: TextView
    private lateinit var date: TextView
    private lateinit var tags: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chosenRecord = it.getParcelable(CHOSEN_RECORD)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name = view.findViewById(R.id.nameValue)
        date = view.findViewById(R.id.dateValue)
        tags = view.findViewById(R.id.tagsValue)

        name.text = chosenRecord.name
        date.text = ImageRecord.DATE_FORMATTER.format(chosenRecord.date)
        tags.text = chosenRecord.tags.toString()
    }


    companion object {
        @JvmStatic
        fun newInstance(chosenRecord: ImageRecord) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CHOSEN_RECORD, chosenRecord)
                }
            }
    }
}
