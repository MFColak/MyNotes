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
import com.google.android.material.textfield.TextInputEditText

class AddNotePopupFragment : DialogFragment() {

    private lateinit var binding: FragmentAddNotePopupBinding
    private lateinit var listener: DialogAddNoteBtnClickListener

    fun setListener(listener: DialogAddNoteBtnClickListener){
        this.listener = listener
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNotePopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvent()
    }

    private fun registerEvent() {
        binding.addNoteButton.setOnClickListener {
            val tittleTask = binding.titleEt.text.toString()
            val noteTask = binding.descriptionEt.text.toString()

            if (tittleTask.isNotEmpty() && noteTask.isNotEmpty()){
                listener.onSaveTask(tittleTask, binding.titleEt, noteTask , binding.descriptionEt)

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
    }


}