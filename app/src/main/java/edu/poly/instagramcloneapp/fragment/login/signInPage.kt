package edu.poly.instagramcloneapp.fragment.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import edu.poly.instagramcloneapp.activity.MainActivity
import edu.poly.instagramcloneapp.activity.login.ProfileStart
import edu.poly.instagramcloneapp.databinding.FragmentSignInPageBinding

class signInPage : Fragment() {

    private lateinit var binding: FragmentSignInPageBinding

    //Chung của Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Chung của SignIn
        binding = FragmentSignInPageBinding.inflate(layoutInflater)
        //Chung của FIrebase
        dbRef = FirebaseDatabase.getInstance().getReference("users")
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

                        val usersRef = FirebaseDatabase.getInstance().getReference("users")

                        //Ktra if Profile Created or not
                        usersRef.child(firebaseAuth.uid.toString()).addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()){
                                    val intent = Intent(requireActivity(), MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                                    startActivity(intent)
                                }else{
                                    val intent = Intent(requireActivity(), ProfileStart::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                                    startActivity(intent)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(requireActivity(), "F1", Toast.LENGTH_SHORT).show()
                            }

                        })



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

