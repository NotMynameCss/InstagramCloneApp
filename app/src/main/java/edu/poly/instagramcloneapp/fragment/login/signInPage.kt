package edu.poly.instagramcloneapp.fragment.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import edu.poly.instagramcloneapp.MainActivity
import edu.poly.instagramcloneapp.R
import edu.poly.instagramcloneapp.databinding.FragmentHomeBinding
import edu.poly.instagramcloneapp.databinding.FragmentSignInPageBinding


class signInPage : Fragment() {

    private lateinit var binding: FragmentSignInPageBinding

    //Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth
    //Firebase Database:
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //firebase Object
        dbRef = FirebaseDatabase.getInstance().getReference("User")
        //Chung của firebase

        binding = FragmentSignInPageBinding.inflate(layoutInflater)
        //Firebase Auth

        firebaseAuth = FirebaseAuth.getInstance()

        //Button


        binding.sumbitLogin.setOnClickListener {
            signin()
        }

        //Cần cho onCreateView()
        return binding.root
    }

    private fun signin(){
        val email_User = binding.emailLogin.text.toString()
        val password = binding.passLogin.text.toString()

        if (email_User.isEmpty() && password.isEmpty()){
            Toast.makeText(requireActivity(), "No Blank Plz", Toast.LENGTH_SHORT).show()
        }
        else{
            firebaseAuth.signInWithEmailAndPassword(email_User,password).addOnCompleteListener{
                if (it.isSuccessful){


                    //Ktra đã xác nhận Email chưa
                    val confirmEmail = firebaseAuth.currentUser?.isEmailVerified
                    if (confirmEmail == true){
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        startActivity(intent)
                    } else{
                        Toast.makeText(requireActivity(), "Your email Not check yet", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(requireActivity(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}