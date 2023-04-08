package edu.poly.instagramcloneapp.fragment.main


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

import edu.poly.instagramcloneapp.databinding.FragmentPersonBinding
import edu.poly.instagramcloneapp.model.UserModel
import java.util.*


//Get Username,emai from currentUser: https://stackoverflow.com/questions/59117267/getting-currently-logged-in-users-id-in-firebase-auth-in-kotlin
//(Use Create) Upload Profile: https://www.youtube.com/watch?v=UDgMEmqEybc&list=PPSV



//(Use update) Update Data Firebase: https://www.youtube.com/watch?v=srQ0Nq3mJ_M
//Read Data: https://www.youtube.com/watch?v=_DtbxQ9EEhc&t=812s

//* Update can alter Create
class person : Fragment() {

    //Chung của Fragment
        private lateinit var binding:FragmentPersonBinding

    //FireBase Database, Cloud Storage: dùng lưu thông tin, ảnh của User
        private lateinit var database: FirebaseDatabase
        private lateinit var storage: FirebaseStorage
        private lateinit var imageUri: Uri
    // Update data FIrebase
        private lateinit var databaseReference: DatabaseReference


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


        //View
            val user_Info = firebaseAuth.currentUser

            binding.emailPerson.setText("${user_Info?.email}")
        //Retrive
            databaseReference = FirebaseDatabase.getInstance().getReference("users")
            databaseReference.child(firebaseAuth.uid.toString()).addValueEventListener(
                object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val user: UserModel? = snapshot.getValue(UserModel::class.java)
                        binding.namePerson.setText(user?.name)


                        Glide.with(requireActivity())
                            .load(user?.imageUrl)
                            .fallback(R.drawable.notification_bg_normal_pressed)
                            .fitCenter()
                            .into(binding.imageView3)


                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )
        //Select Image (Yes)
            binding.imageView3.setOnClickListener{
                pickImage()
            }
        //Upload Profile / 11:04
            binding.sumbitProfile.setOnClickListener {
                if(binding.namePerson.text!!.isEmpty()){
                    Toast.makeText(requireActivity(), "F1", Toast.LENGTH_SHORT).show()

                }else{
                    uploadInfo()
                }


//                else{
//                    uploadInfo()
//                }

            }
        //test Upload Profile



    return binding.root
    }


    //Upload Data profile/ 11:06
    private fun uploadData() {

            val reference = storage.reference.child("Profile").child(Date().time.toString())
            reference.putFile(imageUri).addOnCompleteListener {
                if (it.isSuccessful){
                    reference.downloadUrl.addOnSuccessListener { task ->
                        uploadInfo2(task.toString())
                    }
                }
            }


    }

    private fun uploadInfo2(imgUrl: String) {



        //Chung cho Update
        databaseReference = Firebase.database.reference
        //Tạo Dữ liệu sẽ Update
        val editMap = mapOf(
            "imageUrl" to imgUrl
        )
        val userId = firebaseAuth.uid
        if (userId != null) {
            databaseReference.child("users").child(userId).updateChildren(editMap)
        }

    }

    //Complete/16:30
    private fun uploadInfo() {


            val user = UserModel(firebaseAuth.uid.toString(),
                binding.namePerson.text.toString(),
                firebaseAuth.currentUser?.email,

            )
//            database.reference.child("users")
//                .child(firebaseAuth.uid.toString())
//                .setValue(user)
//                .addOnSuccessListener {
//                    Toast.makeText(requireActivity(), "Success", Toast.LENGTH_SHORT).show()
//
//                }
//        Chung CHo Update
        databaseReference = Firebase.database.reference
        //Tạo dữ liệu Sẽ Update
        val editMap = mapOf(
            "name" to user.name,
            "email" to user.email,


        )
        val userId = firebaseAuth.uid
        if (userId != null) {
            databaseReference.child("users").child(userId).updateChildren(editMap)
        }

    }

    private fun pickImage() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       if (data != null){
           if (requestCode == 100 && resultCode == Activity.RESULT_OK){ //Acitivity is need if this is fragment
               imageUri = data.data!!

               binding.imageView3.setImageURI(imageUri)
               uploadData()
           }
       }

    }



}