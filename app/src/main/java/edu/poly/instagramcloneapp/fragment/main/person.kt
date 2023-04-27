package edu.poly.instagramcloneapp.fragment.main


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import edu.poly.instagramcloneapp.activity.person.changeImage

import edu.poly.instagramcloneapp.databinding.FragmentPersonBinding
import edu.poly.instagramcloneapp.model.UserModel


//Get Username,emai from currentUser: https://stackoverflow.com/questions/59117267/getting-currently-logged-in-users-id-in-firebase-auth-in-kotlin
//(Use Create) Upload Profile: https://www.youtube.com/watch?v=UDgMEmqEybc&list=PPSV



//(Use update) Update Data Firebase: https://www.youtube.com/watch?v=srQ0Nq3mJ_M
//Read Data: https://www.youtube.com/watch?v=_DtbxQ9EEhc&t=812s

//* Update can alter Create
class person : Fragment() {

    //Chung của Fragment
        private lateinit var binding:FragmentPersonBinding

    //Chung Của Firebase
        private lateinit var database: FirebaseDatabase
        private lateinit var storage: FirebaseStorage
        private lateinit var databaseReference: DatabaseReference
        //For SelectImage
        private lateinit var imageUri: Uri
        //Firebase Auth: ACcount
        private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Chung của Person
            binding = FragmentPersonBinding.inflate(layoutInflater)
            firebaseAuth = FirebaseAuth.getInstance()
            storage = FirebaseStorage.getInstance()
            database = FirebaseDatabase.getInstance()

        //Retrive for Info
        retrivieInfo()

        //Select Image
            binding.imageView3.setOnClickListener{
//                pickImage()

                val intent = Intent(requireActivity(), changeImage::class.java)
                //fix cho fragment not attachted
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
            }
        //Upload Profile Info
            binding.sumbitProfile.setOnClickListener {
                if(binding.namePerson.text!!.isEmpty()){
                    Toast.makeText(requireActivity(), "F1", Toast.LENGTH_SHORT).show()
                }else{
                    uploadInfo()
                }
            }
    return binding.root
    }

    private fun retrivieInfo() {
        //Retrive for Info
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.child(firebaseAuth.uid.toString()).addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    val user: UserModel? = snapshot.getValue(UserModel::class.java)
                    binding.namePerson.setText(user?.name)
                    binding.emailPerson.setText(user?.email)
                    //For Fix Null Image Crash
                    Glide.with(requireActivity())
                        .load(user?.imageUrl)
                        .fitCenter()
                        .fallback(R.drawable.notification_bg_normal_pressed)
                        .into(binding.imageView3)
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )
    }

    //Upload Info của Profile User
    private fun uploadInfo() {
        //Chung CHo Update
        databaseReference = Firebase.database.getReference("users")

            val user = UserModel(
                name = binding.namePerson.text.toString(),
                email = firebaseAuth.currentUser?.email,
            )
        //Tạo dữ liệu Sẽ Update
        val editMap = mapOf(
            "name" to user.name,
            "email" to user.email,
        )
        val userId = firebaseAuth.uid
        if (userId != null) {
            databaseReference.child(userId).updateChildren(editMap)
        }

    }

}