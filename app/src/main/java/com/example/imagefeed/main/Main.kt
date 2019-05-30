package com.example.imagefeed.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imagefeed.R
import com.example.imagefeed.addimage.AddImage
import com.example.imagefeed.model.ImageRecord
import com.example.imagefeed.photoactivity.PhotoActivity
import java.util.*

class Main : AppCompatActivity() {

    companion object {
        private const val ADD_IMAGE_REQUEST_CODE = 57
    }

    private val recordList: MutableList<ImageRecord> = mutableListOf()

    private lateinit var myRecycler: RecyclerView
    private lateinit var myAdapter: MainAdapter
    private lateinit var myLayoutManager: RecyclerView.LayoutManager

    private val itemTouchHelperCallback =
        object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if(direction == ItemTouchHelper.RIGHT) {
                myAdapter.removeRecord(viewHolder)
            }
            else if(direction == ItemTouchHelper.LEFT) {
                val chosenRecord: ImageRecord = recordList[viewHolder.adapterPosition]

                val intent = Intent(applicationContext, PhotoActivity::class.java)
                intent.putExtra(PhotoActivity.CHOSEN_RECORD, chosenRecord)
                intent.putStringArrayListExtra(PhotoActivity.RELEVANT_PHOTOS, getRelevantPhotoUrls(chosenRecord))
                startActivity(intent)

                myAdapter.notifyItemChanged(viewHolder.adapterPosition)
            }
        }
    }
    private val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        initializeData()

        myLayoutManager = LinearLayoutManager(this)
        myAdapter = MainAdapter(recordList)

        myRecycler = findViewById<RecyclerView>(R.id.mainRecycler).apply {
            setHasFixedSize(true)
            layoutManager = myLayoutManager
            adapter = myAdapter
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }

        itemTouchHelper.attachToRecyclerView(myRecycler)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item!!.itemId) {
            R.id.mainMenuAddImageItem -> {
                val intent = Intent(this, AddImage::class.java)
                startActivityForResult(intent, ADD_IMAGE_REQUEST_CODE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == ADD_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val record = data!!.getParcelableExtra<ImageRecord>(AddImage.RESULT_RECORD_KEY)

                recordList.add(0, record)
                myRecycler.adapter!!.notifyItemInserted(0)
            }
        }
    }

    private fun initializeData() {


        recordList.add(
            ImageRecord("http://www.sobi.org/photos/Cat/Istanbul/SofiaAssorted/SofiaCats_017.jpg",
                "cat one",
                Date(119, 5, 5),
                listOf("Cat", "Pet", "Fur")
            )
        )

        recordList.add(
            ImageRecord(
                "https://images.wagwalkingweb.com/media/articles/cat/diarrhea/diarrhea.jpg",
                "cat two",
                Date(118, 4, 12),
                listOf("Cat", "Fur", "Animal")
            )
        )

        recordList.add(
            ImageRecord(
                "http://www.pets4homes.co.uk/images/breeds/35/large/c7a17b1cd1d5a7df28f71dbfc443b71c.jpg",
                "hello",
                Date(118, 10, 24),
                listOf("Room", "Cat", "Funny")
            )
        )

        recordList.add(
            ImageRecord(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e1/Android_dance.svg/2000px-Android_dance.svg.png",
                "android",
                Date(119, 5, 9),
                listOf("Android", "Drawing", "Green")
            )
        )

        recordList.add(
            ImageRecord(
                "https://http.cat/404",
                "cat not found",
                Date(119, 3, 2),
                listOf("Animal", "Fur")
            )
        )

        recordList.add(
            ImageRecord(
                "https://http.cat/200",
                "cat is here",
                Date(117, 10, 2),
                listOf("Pet", "Animal", "Dog")
            )
        )

        recordList.add(
            ImageRecord(
                "https://http.cat/429",
                "another",
                Date(118, 3, 15),
                listOf("Cats", "Animal", "Outdoors")
            )
        )
    }

    private fun getRelevantPhotoUrls(chosenRecord: ImageRecord): ArrayList<String> {
        val sortedList = recordList.sortedByDescending { it.numberOfTagsInCommon(chosenRecord, it) }

        return sortedList.take(6).map { record -> record.url } as ArrayList<String>
    }
}
