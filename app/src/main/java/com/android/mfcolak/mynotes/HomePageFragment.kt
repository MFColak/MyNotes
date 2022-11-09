package com.android.mfcolak.mynotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mfcolak.mynotes.databinding.EachItemBinding
import com.android.mfcolak.mynotes.databinding.FragmentHomePageBinding
import com.android.mfcolak.mynotes.util.MyNotesAdapter
import com.android.mfcolak.mynotes.util.MyNotesData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.jvm.internal.Ref.ObjectRef

class HomePageFragment : Fragment(), AddNotePopupFragment.DialogAddNoteBtnClickListener,
    MyNotesAdapter.MyNotesAdapterClicksInterface {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomePageBinding
    private var popupFragment: AddNotePopupFragment?= null
    private lateinit var adapter: MyNotesAdapter
    private lateinit var mutableList: MutableList<MyNotesData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        getDataFromFirebase()
        newNoteEvents()
    }

    private fun newNoteEvents() {
        binding.addNoteBtn.setOnClickListener{
            if (popupFragment != null)
                childFragmentManager.beginTransaction().remove(popupFragment!!).commit()
            popupFragment = AddNotePopupFragment()
            popupFragment!!.setListener(this)
            popupFragment!!.show(
                childFragmentManager,
                AddNotePopupFragment.TAG
            )
        }
    }


    private fun init(view: View){
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("Tasks")
            .child(auth.currentUser?.uid.toString())

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        mutableList = mutableListOf()
        adapter = MyNotesAdapter(mutableList)
        adapter.setListener(this)
        binding.recyclerView.adapter = adapter
    }

    private fun getDataFromFirebase(){
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                mutableList.clear()
                for (taskSnapshot in snapshot.children){

                    val mynotesTask = taskSnapshot.key?.let {
                        MyNotesData(it, taskSnapshot.child("Title").value.toString(), taskSnapshot.child("Description").value.toString())
                    }

                    if (mynotesTask != null){
                        mutableList.add(mynotesTask)
                    }

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSaveTask(
        title: String,
        titleEditText: TextInputEditText,
        note: String,
        noteEditText: TextInputEditText
    ) {


        val hashMap:HashMap<String,String> = HashMap<String,String>(3)
        hashMap["Title"] = title
        hashMap["Description"] = note

        databaseReference.push().setValue(hashMap).addOnCompleteListener{
            if (it.isSuccessful){

                Toast.makeText(context, "Note saved succesfully", Toast.LENGTH_SHORT).show()


            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            titleEditText.text = null
            noteEditText.text = null
            popupFragment?.dismiss()
        }
    }

    override fun onUpdateTask(
        myNotesData: MyNotesData,
        titleEditText: TextInputEditText,
        myNotesData2: MyNotesData,
        noteEditText: TextInputEditText
    ) {
        val map = HashMap<String, Any>()
       // map[myNotesData.taskId] = myNotesData.taskId
        map["Title"] = myNotesData.Title
        map["Description"] = myNotesData2.Description
        databaseReference.updateChildren(map).addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            titleEditText.text = null
            noteEditText.text = null
            popupFragment!!.dismiss()
        }
    }

    override fun onDeleteTaskBtnClicked(myNotesData: MyNotesData) {
        databaseReference.child(myNotesData.taskId).removeValue().addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(context,"Note is deleted", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onEditTaskBtnClicked(myNotesData: MyNotesData) {
        if (popupFragment != null){
            childFragmentManager.beginTransaction().remove(popupFragment!!).commit()

            popupFragment = AddNotePopupFragment.newInstance(myNotesData.taskId, myNotesData.Title, myNotesData.Description)
            popupFragment!!.setListener(this)
            popupFragment!!.show(childFragmentManager, AddNotePopupFragment.TAG)
        }
    }

}