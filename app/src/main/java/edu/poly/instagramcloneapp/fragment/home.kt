package edu.poly.instagramcloneapp.fragment

// Connect firebase:  https://www.youtube.com/watch?v=YOT8P1PtJQg&t=618s
//Viewbinding in fragment: https://www.youtube.com/watch?v=v11x54y5YVc
//SignIn and SignOut : https://www.youtube.com/watch?v=idbxxkF1l6k&list=PLQFUWT9wUMWEXj9AVanMZg1tRUGr95aBr&index=10&t=784s

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
import edu.poly.instagramcloneapp.databinding.FragmentHomeBinding
import edu.poly.instagramcloneapp.model.UserModel


class home : Fragment() {

    private lateinit var binding: FragmentHomeBinding

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

            binding = FragmentHomeBinding.inflate(layoutInflater)
        //Firebase Auth

            firebaseAuth = FirebaseAuth.getInstance()

        //Button
            binding.Send.setOnClickListener {
            saveUserData()
        }
            binding.SignUp.setOnClickListener {
            signup()
        }
        binding.signIn.setOnClickListener {
            signin()
        }

        //Cần cho onCreateView()
            return binding.root
    }
    //Data for firebase
    private fun saveUserData(){
        val email_User = binding.EmailInput.text.toString()

        val user_id = dbRef.push().key!!

        // Push to model in UserModel
        val users = UserModel(user_id,email_User)

        dbRef.child(user_id).setValue(users)

    }

    //Login For Firebase
    private fun signup(){
        val email_User = binding.EmailInput.text.toString()
        val password = binding.passwordInput.text.toString()

        if (email_User.isEmpty() && password.isEmpty()){
            Toast.makeText(requireActivity(), "No Blank Plz", Toast.LENGTH_SHORT).show()
        }
        else{
            firebaseAuth.createUserWithEmailAndPassword(email_User,password).addOnCompleteListener{
                if (it.isSuccessful){
                    Toast.makeText(requireActivity(), "Login Complete", Toast.LENGTH_SHORT).show()

                }
                else{

                    Toast.makeText(requireActivity(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun signin(){
        val email_User = binding.EmailInput.text.toString()
        val password = binding.passwordInput.text.toString()

        if (email_User.isEmpty() && password.isEmpty()){
            Toast.makeText(requireActivity(), "No Blank Plz", Toast.LENGTH_SHORT).show()
        }
        else{
            firebaseAuth.signInWithEmailAndPassword(email_User,password).addOnCompleteListener{
                if (it.isSuccessful){
                    Toast.makeText(requireActivity(), "Login Complete", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                }
                else{

                    Toast.makeText(requireActivity(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}