package com.allie.pre90secs.fragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.allie.pre90secs.R
import com.allie.pre90secs.adapter.InstructionAdapter
import com.allie.pre90secs.model.ExerciseItem
import com.google.gson.Gson
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.Camera
import android.net.Uri
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import com.squareup.picasso.Picasso
import java.io.*
import java.util.*


class AddCustomWorkoutFragment : Fragment() {
    val TAG = this.tag
    val MY_WORKOUTS_PREFS_FILE = "MyWorkoutsPrefs"
    private var myPrefs: SharedPreferences? = null

    private lateinit var workoutImage: ImageView
    private lateinit var addWorkoutImage: ImageButton
    private lateinit var titleEditText: EditText
    private lateinit var instructionRecyclerView: RecyclerView
    private lateinit var addInstructionButton: ImageView
    private lateinit var instructionEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var upperBodyCheck: CheckBox
    private lateinit var lowerBodyCheck: CheckBox
    private lateinit var coreCheck: CheckBox
    private lateinit var cardioCheck: CheckBox
    private lateinit var wholeBodyCheck: CheckBox
    private lateinit var limitedSpaceCheck: CheckBox
    private lateinit var easyCheckBox: CheckBox
    private lateinit var mediumCheckBox: CheckBox
    private lateinit var hardCheckBox: CheckBox
    private var camera: Camera? = null

    private val PERMISSION_CAMERA = 1
    private val PERMISSION_READ_EXTERNAL = 2
    private val PERMISSION_WRITE_EXTERNAL = 3

    private var bitmap: Bitmap? = null
    private var newInstructionText: String? = null
    private var newTitleText: String? = null
    private var newImage: String? = null
    private var newBodyRegions: MutableList<String> = mutableListOf()
    private var newDifficultyLevels: MutableList<String> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.add_custom_workout_fragment, container, false)
        workoutImage = v.findViewById(R.id.workout_image) as ImageView
        addWorkoutImage = v.findViewById(R.id.add_workout_image)
        titleEditText = v.findViewById(R.id.add_title_edit_text)
        instructionRecyclerView = v.findViewById(R.id.add_instructions_list)
        addInstructionButton = v.findViewById(R.id.add_instruction_button)
        saveButton = v.findViewById(R.id.save_button)
        instructionEditText = v.findViewById(R.id.enter_instruction_editText)
        upperBodyCheck = v.findViewById<View>(R.id.upperRegionCheckBox) as CheckBox
        lowerBodyCheck = v.findViewById<View>(R.id.lowerRegionCheckBox) as CheckBox
        wholeBodyCheck = v.findViewById<View>(R.id.wholeBodyRegionCheckBox) as CheckBox
        cardioCheck = v.findViewById<View>(R.id.cardioRegionCheckBox) as CheckBox
        coreCheck = v.findViewById<View>(R.id.coreRegionCheckBox) as CheckBox
        limitedSpaceCheck = v.findViewById<View>(R.id.limitedSpaceCheckBox) as CheckBox
        easyCheckBox = v.findViewById(R.id.easy_check_box)
        mediumCheckBox = v.findViewById(R.id.medium_check_box)
        hardCheckBox = v.findViewById(R.id.hard_check_box)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setCheckBoxChangeListeners()

        myPrefs = this.activity?.applicationContext?.getSharedPreferences(MY_WORKOUTS_PREFS_FILE, MODE_PRIVATE)

        val instructionAdapter = InstructionAdapter(ArrayList<String>(), activity)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        instructionRecyclerView.layoutManager = layoutManager
        instructionRecyclerView.adapter = instructionAdapter

        addWorkoutImage.setOnClickListener {
            val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this.activity as AppCompatActivity, permissions, 0)

            if (ContextCompat.checkSelfPermission(this.context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this.activity!!, permissions, PERMISSION_CAMERA)
            }
            if (ContextCompat.checkSelfPermission(this.context!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this.activity!!, permissions, PERMISSION_READ_EXTERNAL)
            }
            if (ContextCompat.checkSelfPermission(this.context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this.activity!!, permissions, PERMISSION_WRITE_EXTERNAL)
            }

            camera = getCameraInstance()
            val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 0)
        }

        titleEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                newTitleText = s.toString()
            }
        })

        instructionEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                newInstructionText = s.toString()
            }
        })

        addInstructionButton.setOnClickListener {
            instructionAdapter.addInstructionItem(newInstructionText)
            instructionEditText.text.clear()
        }

        saveButton.setOnClickListener {
            val newExerciseItem = ExerciseItem()
            newExerciseItem.instructions = instructionAdapter.instructionList
            newExerciseItem.title = newTitleText
            newExerciseItem.image = newImage
            newExerciseItem.bodyRegion = newBodyRegions
            newExerciseItem.difficulty = newDifficultyLevels
            newExerciseItem.space = limitedSpaceCheck.isChecked

            bitmap?.let { it1 -> storeImage(it1) }

            val prefsEditor = myPrefs?.edit()
            val gson = Gson()
            val json = gson.toJson(newExerciseItem)
            prefsEditor?.putString(newExerciseItem.title, json)
            prefsEditor?.apply()

            activity?.onBackPressed()
        }

    }

    private fun setCheckBox(isChecked: Boolean, name: String) {
        if (isChecked) {
            newBodyRegions.add(name)
        } else {
            newBodyRegions.remove(name)
        }
    }

    private fun setLimitedSpace(b: Boolean) {
        limitedSpaceCheck.isChecked = b
    }

    private fun setDifficulty(isChecked: Boolean, name: String) {
        if (isChecked) {
            newDifficultyLevels.add(name)
        } else {
            newDifficultyLevels.remove(name)
        }
    }

    private fun setCheckBoxChangeListeners() {
        easyCheckBox.setOnCheckedChangeListener { buttonView, isChecked -> setDifficulty(isChecked, "easy") }
        mediumCheckBox.setOnCheckedChangeListener { buttonView, isChecked -> setDifficulty(isChecked, "medium") }
        hardCheckBox.setOnCheckedChangeListener { buttonView, isChecked -> setDifficulty(isChecked, "hard") }

        upperBodyCheck.setOnCheckedChangeListener { buttonView, isChecked -> setCheckBox(isChecked, "upper") }
        lowerBodyCheck.setOnCheckedChangeListener { buttonView, isChecked -> setCheckBox(isChecked, "lower") }
        wholeBodyCheck.setOnCheckedChangeListener { buttonView, isChecked -> setCheckBox(isChecked, "whole") }
        cardioCheck.setOnCheckedChangeListener { buttonView, isChecked -> setCheckBox(isChecked, "cardio") }
        coreCheck.setOnCheckedChangeListener { buttonView, isChecked -> setCheckBox(isChecked, "core") }

        limitedSpaceCheck.setOnCheckedChangeListener { buttonView, isChecked -> setLimitedSpace(isChecked) }
    }

    override fun onPause() {
        super.onPause()
        // Stop camera access
        releaseCamera()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                addWorkoutImage.visibility = View.GONE
                workoutImage.visibility = View.VISIBLE

                bitmap = data?.extras?.get("data") as Bitmap
                workoutImage.setImageBitmap(bitmap)
            }
        }
    }

    private fun getOutputMediaFile(): File? {
        val mediaStorageDir = File(Environment.getExternalStorageDirectory().toString()
                + "/Android/data/"
                + activity?.applicationContext?.packageName
                + "/Files")

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        val mediaFile: File
        val mImageName = "$newTitleText.jpg"
        mediaFile = File(mediaStorageDir.path + File.separator + mImageName)
        return mediaFile
    }

    private fun storeImage(image: Bitmap) {
        val pictureFile = getOutputMediaFile()
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ")
            return
        }
        try {
            val fos = FileOutputStream(pictureFile)
            image.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
        } catch (e: FileNotFoundException) {
            Log.d(TAG, "File not found: " + e.localizedMessage)
        } catch (e: IOException) {
            Log.d(TAG, "Error accessing file: " + e.localizedMessage)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CAMERA ->
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    camera = getCameraInstance()

                    val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, 0)
                }
        }
    }

    fun getCameraInstance(): Camera? {
        var c: Camera? = null
        try {
            c = Camera.open()
        } catch (e: Exception) {
            Log.d(TAG, e.localizedMessage)
        }

        return c
    }

    private fun releaseCamera() {
        if (camera != null) {
            camera?.release()
            camera = null
        }
    }
}