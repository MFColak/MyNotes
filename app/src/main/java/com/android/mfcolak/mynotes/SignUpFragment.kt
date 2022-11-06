package com.android.mfcolak.mynotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.android.mfcolak.mynotes.databinding.FragmentSignUpBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth


class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()
    }

    private fun registerEvents() {

        binding.authTextView.setOnClickListener{
            navController.navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        binding.signUpBtn.setOnClickListener{
            val email = binding.emailEditText.text.toString().trim()
            val pass = binding.passEditText.text.toString().trim()
            val verifyPass = binding.rePassEditText.text.toString().trim()

            binding.progressBar.visibility = View.VISIBLE

            if (email.isNotEmpty() && pass.isNotEmpty() && verifyPass.isNotEmpty()){

                if (pass == verifyPass){
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(
                        OnCompleteListener {

                            if (it.isSuccessful){

                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show()
                                navController.navigate(R.id.action_signUpFragment_to_homePageFragment)
                            }
                            else{
                                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                                binding.progressBar.visibility = View.GONE
                            }
                        })
                }else{
                    Toast.makeText(context, "Passwords must be the same and least 6 character", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }

            }else{
                Toast.makeText(context, "These fields cannot be left blank.", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun init(view: View){
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }

}