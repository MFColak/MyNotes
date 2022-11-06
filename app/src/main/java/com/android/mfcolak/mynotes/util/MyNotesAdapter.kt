package com.android.mfcolak.mynotes.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.mfcolak.mynotes.databinding.EachItemBinding

class MyNotesAdapter(private val list: MutableList<MyNotesData>): RecyclerView.Adapter<MyNotesAdapter.MyNotesViewHolder>() {

    private var listener: MyNotesAdapterClicksInterface? = null
    fun setListener(listener: MyNotesAdapterClicksInterface){
        this.listener = listener
    }
       inner class MyNotesViewHolder(val binding: EachItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNotesViewHolder {
       val binding = EachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyNotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyNotesViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.titleTask.text = this.Title
                binding.descriptionTask.text = this.Description

                binding.deleteTask.setOnClickListener {
                    listener?.onDeleteTaskBtnClicked(this)
                }

                binding.editTask.setOnClickListener {
                    listener?.onEditTaskBtnClicked(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface MyNotesAdapterClicksInterface{
        fun onDeleteTaskBtnClicked(myNotesData: MyNotesData)
        fun onEditTaskBtnClicked(myNotesData: MyNotesData)
    }
}