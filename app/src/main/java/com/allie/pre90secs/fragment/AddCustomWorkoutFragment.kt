package com.allie.pre90secs.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.allie.pre90secs.R
import com.allie.pre90secs.adapter.InstructionAdapter

class AddCustomWorkoutFragment:Fragment(){

    private lateinit var addPhotoImage:ImageButton
    private lateinit var titleEditText:EditText
    private lateinit var instructionRecyclerView:RecyclerView
    private lateinit var addInstructionButton:ImageButton
    private lateinit var saveButton:Button
    private var instructionList:MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.add_custom_workout_fragment, container, false)
        addPhotoImage = v.findViewById(R.id.add_workout_image)
        titleEditText = v.findViewById(R.id.add_title_edit_text)
        instructionRecyclerView = v.findViewById(R.id.add_instructions_list)
        addInstructionButton = v.findViewById(R.id.add_instruction_button)
        saveButton = v.findViewById(R.id.save_button)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var instructionAdapter = InstructionAdapter(instructionList, activity)
        var layoutManager:RecyclerView.LayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        instructionRecyclerView.layoutManager = layoutManager
        instructionRecyclerView.adapter = instructionAdapter

        addPhotoImage.setOnClickListener{
            //TODO open camera
        }

        titleEditText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        saveButton.setOnClickListener {  }
    }


}