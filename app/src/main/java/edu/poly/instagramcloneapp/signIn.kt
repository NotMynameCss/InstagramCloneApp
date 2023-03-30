package edu.poly.instagramcloneapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import edu.poly.instagramcloneapp.databinding.ActivitySignInBinding
import edu.poly.instagramcloneapp.model.UserModel

class signIn : AppCompatActivity() {


    private lateinit var binding: ActivitySignInBinding //need for Button and other Binding method

    //Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth
    //Firebase Database:
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_in)

        //firebase Object
        dbRef = FirebaseDatabase.getInstance().getReference("User")
        //Chung của firebase

        binding = ActivitySignInBinding.inflate(layoutInflater)
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

        //De setContentView(binding.root) o cuoi
        setContentView(binding.root)

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
            Toast.makeText(this, "No Blank Plz", Toast.LENGTH_SHORT).show()
        }
        else{
            firebaseAuth.createUserWithEmailAndPassword(email_User,password).addOnCompleteListener{
                if (it.isSuccessful){
                    Toast.makeText(this, "Login Complete", Toast.LENGTH_SHORT).show()

                }
                else{

                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun signin(){
        val email_User = binding.EmailInput.text.toString()
        val password = binding.passwordInput.text.toString()

        if (email_User.isEmpty() && password.isEmpty()){
            Toast.makeText(this, "No Blank Plz", Toast.LENGTH_SHORT).show()
        }
        else{
            firebaseAuth.signInWithEmailAndPassword(email_User,password).addOnCompleteListener{
                if (it.isSuccessful){
                    Toast.makeText(this, "Login Complete", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else{

                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}