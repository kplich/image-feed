package com.example.imagefeed.addimage

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import com.example.imagefeed.R
import com.example.imagefeed.model.ImageRecord
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.add_image_activity.*
import java.util.*

class AddImage : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    companion object {
        const val RESULT_RECORD_KEY = "result"
        const val TAG_DELIMITER = ","
        val LABELER: FirebaseVisionImageLabeler = FirebaseVision.getInstance().onDeviceImageLabeler
    }

    private var inputDate = Date()
    private var isDateValid = true

    private lateinit var tags: List<String>
    private val target = object: Target {
        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

        override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
            tags = emptyList()
        }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            addImageImagePreview.setImageBitmap(bitmap)

            val firebaseImage = FirebaseVisionImage.fromBitmap(bitmap!!)

            LABELER.processImage(firebaseImage)
                .addOnSuccessListener {
                    tags = it.map { label -> label.text }
                    addImageTagsPreview.text = tags.joinToString(TAG_DELIMITER)
                }
                .addOnFailureListener {
                    Log.wtf("LAB", it.message)
                }
        }
    }

    /**
     * Listener that loads picture after the URL input field has been left and became unfocused.
     */
    private val loadPictureListener = View.OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            if(addImageUrlInput.text.toString().isNotEmpty()) {
                Log.d("LAB", "focus changed")
                Picasso.get().isLoggingEnabled = true

                Picasso.get()
                    .load(addImageUrlInput.text.toString())
                    .into(target)
            }
        }
    }

    /**
     * Listener for validating date after it has been set.
     */
    private val testDateListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            val date = inputDate
            if(date.after(Date())) {
                addImageDatePreview.requestFocus()
                addImageDatePreview.error = getString(R.string.add_image_future_date)
                isDateValid = false
            }
            else {
                addImageDatePreview.error = null
                isDateValid = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_image_activity)

        addImageDatePreview.text = ImageRecord.DATE_FORMATTER.format(inputDate)
        addImageUrlInput.onFocusChangeListener = loadPictureListener
        addImageDatePreview.addTextChangedListener(testDateListener)
    }

    fun onAddImageClicked(view: View) {
        val url = validateInput(
            addImageUrlInput,
            getString(R.string.app_default_invalid_input),
            {s: String -> s},
            Pair({s: String -> s.isNotEmpty()}, getString(R.string.add_image_empty_input))
            )

        val name = validateInput(
            addImageNameInput,
            getString(R.string.app_default_invalid_input),
            {s: String -> s},
            Pair({s: String -> s.isNotEmpty()}, getString(R.string.add_image_empty_input))
        )

        if(url != null && name != null && isDateValid) {
            val result = ImageRecord(url, name, inputDate, tags)

            val resultIntent = Intent()
            resultIntent.putExtra(RESULT_RECORD_KEY, result)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    fun onAddImageDatePickerClicked(view: View) {
        val datePicker = DatePickerFragment()
        datePicker.show(supportFragmentManager, "inputDate picker")
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        inputDate = c.time
        addImageDatePreview.text = ImageRecord.DATE_FORMATTER.format(inputDate)
    }

    /**
     * Method for validating input from a field with given conditions.
     * @param field TextField from which a value is read
     * @param errorMessage general error message displayed when reading input fails
     * @param fromString function converting a string to an object of a desired type
     * @param validationRules pairs comprised of functions validating the result and corresponding error messages
     * if validation fails
     * @return number conforming to given conditions or null when input doesn't conform to constraints
     */
    private fun <R> validateInput(field: EditText, errorMessage: String, fromString: (String) -> (R), vararg validationRules: Pair<(R) -> (Boolean), String>): R? {
        var result: R? = null
        var ruleBroken = false

        try {
            result = fromString(field.text.toString())


            for (rule in validationRules) {
                if (!rule.first(result)) {
                    field.error = rule.second
                    ruleBroken = true
                    break
                }
            }
        } catch (e: Exception) {
            field.error = errorMessage
        }

        return if (ruleBroken) null else result
    }
}
