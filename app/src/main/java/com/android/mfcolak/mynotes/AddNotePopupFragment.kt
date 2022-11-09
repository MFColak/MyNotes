package com.android.mfcolak.mynotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.android.mfcolak.mynotes.databinding.FragmentAddNotePopupBinding
import com.android.mfcolak.mynotes.util.MyNotesData
import com.google.android.material.textfield.TextInputEditText

class AddNotePopupFragment : DialogFragment() {

    private lateinit var binding: FragmentAddNotePopupBinding
    private lateinit var listener: DialogAddNoteBtnClickListener
    private var myNotesData: MyNotesData? = null

    fun setListener(listener: DialogAddNoteBtnClickListener){
        this.listener = listener
    }

    companion object{
        const val TAG ="AddNotePopupFragment"

        @JvmStatic
        fun newInstance(taskId: String, title: String, description: String) = AddNotePopupFragment().apply {
            arguments = Bundle().apply {
                putString("taskId", taskId)
                putString("Title", title)
                putString("Description", description)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNotePopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null){
            myNotesData = MyNotesData(arguments?.getString("taskId").toString(),
                arguments?.getString("Title").toString(),
                arguments?.getString("Description").toString()
            )
            binding.titleEt.setText(myNotesData?.Title)
            binding.descriptionEt.setText(myNotesData?.Description)
        }
        registerEvent()
    }

    private fun registerEvent() {
        binding.addNoteButton.setOnClickListener {
            val tittleTask = binding.titleEt.text.toString()
            val noteTask = binding.descriptionEt.text.toString()

            if (tittleTask.isNotEmpty() && noteTask.isNotEmpty()){
                if(myNotesData == null){
                    listener.onSaveTask(tittleTask, binding.titleEt, noteTask , binding.descriptionEt)
                }else{
                    myNotesData?.Title = tittleTask
                    myNotesData?.Description = noteTask
                    listener.onUpdateTask(myNotesData!!, binding.titleEt, myNotesData!!,binding.descriptionEt)
                }


            } else{
                Toast.makeText(context, "Please type some task", Toast.LENGTH_SHORT).show()
            }
        }
        binding.noteClose.setOnClickListener {
            dismiss()
        }
    }

    interface DialogAddNoteBtnClickListener{
        fun onSaveTask(title: String,  titleEditText: TextInputEditText, note: String, noteEditText: TextInputEditText )
        fun onUpdateTask(myNotesData: MyNotesData,  titleEditText: TextInputEditText, myNotesData2: MyNotesData, noteEditText: TextInputEditText )

    }


}