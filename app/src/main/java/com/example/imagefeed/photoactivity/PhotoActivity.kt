package com.example.imagefeed.photoactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ViewSwitcher
import com.example.imagefeed.R
import com.example.imagefeed.model.ImageRecord
import com.example.imagefeed.photoactivity.fragments.DetailsFragment
import com.example.imagefeed.photoactivity.fragments.PhotoOnlyFragment
import com.example.imagefeed.photoactivity.fragments.relevantphotos.RelevantPhotosFragment
import kotlin.math.abs

class PhotoActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    companion object {
        const val CHOSEN_RECORD = "CHOSEN_RECORD"
        const val RELEVANT_PHOTOS = "RELEVANT_PHOTOS"
    }

    // vars
    private lateinit var detector: GestureDetector
    private lateinit var switcher: ViewSwitcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        val chosenRecord = intent!!.extras!!.getParcelable<ImageRecord>(CHOSEN_RECORD)!!
        val relevantPhotos = intent!!.extras!!.getStringArrayList(RELEVANT_PHOTOS)!!

        detector = GestureDetector(this, this)
        switcher = findViewById(R.id.switcher)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.photo, PhotoOnlyFragment.newInstance(chosenRecord))
            replace(R.id.details, DetailsFragment.newInstance(chosenRecord))
            replace(R.id.relevantPhotos, RelevantPhotosFragment.newInstance(relevantPhotos))
            commit()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        detector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun onShowPress(e: MotionEvent?) {}

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return false
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        val xAbsoluteDiff = abs(e1!!.rawX - e2!!.rawX)
        val yAbsoluteDiff = abs(e1.rawY - e2.rawY)

        if(switcher.currentView.id == R.id.photoView) {
            //
            if (e1.rawX > e2.rawX && xAbsoluteDiff > 5 * yAbsoluteDiff) {
                switcher.showNext()
            }
        }
        else if(switcher.currentView.id == R.id.detailsView) {
            if (e1.rawX < e2.rawY && xAbsoluteDiff > 5 * yAbsoluteDiff) {
                switcher.showPrevious()
            }
        }

        return false
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent?) {}
}
