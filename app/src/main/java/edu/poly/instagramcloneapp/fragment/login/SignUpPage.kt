package edu.poly.instagramcloneapp.fragment.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import edu.poly.instagramcloneapp.databinding.FragmentSignUpPageBinding
import edu.poly.instagramcloneapp.activity.login.login
//Email Verification: https://www.youtube.com/watch?v=8wQ8QacTIVA&t=389s

class SignUpPage : Fragment() {

    private lateinit var binding: FragmentSignUpPageBinding

    //Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Chung của firebase

            binding = FragmentSignUpPageBinding.inflate(layoutInflater)
            firebaseAuth = FirebaseAuth.getInstance()

        //Button

            binding.sumbitSignUp.setOnClickListener {
            signup()
        }

        //Cần cho onCreateView()
        return binding.root
    }

    private fun signup(){
        val email_User = binding.emailSignUp.text.toString()
        val password1 = binding.pass1SignUp.text.toString()
        val password2 = binding.pass2SignUp.text.toString()



         if (email_User.isNotEmpty() && password1 == password2 && password1.isNotEmpty()){
            firebaseAuth.createUserWithEmailAndPassword(email_User,password1).addOnCompleteListener{
                if (it.isSuccessful){

                    firebaseAuth.currentUser?.sendEmailVerification()
                        ?.addOnSuccessListener {
                            Toast.makeText(requireActivity(),"SignUp,Plz Check Email",Toast.LENGTH_SHORT).show()
                        }
                        ?.addOnFailureListener {
                            Toast.makeText(requireActivity(),it.toString(),Toast.LENGTH_SHORT).show()
                        }
                    //Đổi Fragment
                    val intent = Intent(requireActivity(), login::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT

                    startActivity(intent)
                }
                else{

                    Toast.makeText(requireActivity(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
        else{
             Toast.makeText(requireActivity(), "Error Login Plz check again", Toast.LENGTH_SHORT).show()
         }
    }
}