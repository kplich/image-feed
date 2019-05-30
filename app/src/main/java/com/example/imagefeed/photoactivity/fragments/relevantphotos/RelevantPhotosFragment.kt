package com.example.imagefeed.photoactivity.fragments.relevantphotos


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.imagefeed.R

private const val RELEVANT_PHOTOS = "RELEVANT_PHOTOS"

class RelevantPhotosFragment : Fragment() {

    // arguments
    private lateinit var relevantPhotos: ArrayList<String>

    // widgets
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: RelevantPhotosAdapter
    private lateinit var layoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            relevantPhotos = it.getStringArrayList(RELEVANT_PHOTOS)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_relevant_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutManager = GridLayoutManager(activity, 3)
        adapter = RelevantPhotosAdapter(relevantPhotos)

        recycler = view.findViewById<RecyclerView>(R.id.relevantPhotos).apply {
            setHasFixedSize(true)
            layoutManager = this@RelevantPhotosFragment.layoutManager
            adapter = this@RelevantPhotosFragment.adapter
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(relevantPhotos: ArrayList<String>) =
            RelevantPhotosFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(RELEVANT_PHOTOS, relevantPhotos)
                }
            }
    }
}
