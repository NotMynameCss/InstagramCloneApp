package edu.poly.instagramcloneapp.activity.person

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import edu.poly.instagramcloneapp.databinding.ActivityChangeImageBinding
import edu.poly.instagramcloneapp.fragment.main.person
import edu.poly.instagramcloneapp.model.UserModel
import java.util.*

//fix You cannot start a load for a destroyed activity:https://stackoverflow.com/questions/40326571/android-glide-erroryou-cannot-start-a-load-for-a-destroyed-activity

class changeImage : AppCompatActivity() {
    private lateinit var binding:ActivityChangeImageBinding

    //Chung Của Firebase
    private lateinit var storage: FirebaseStorage
    private lateinit var databaseReference: DatabaseReference
    //For SelectImage
    private lateinit var imageUri: Uri
    //Firebase Auth: ACcount
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangeImageBinding.inflate(layoutInflater)

        firebaseAuth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        retrivieInfo()

        binding.imageView.setOnClickListener {
            pickImage()
        }
        binding.button2.setOnClickListener{
            finish()
        }

        setContentView(binding.root)
    }

    private fun retrivieInfo() {
        //Retrive for Info
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.child(firebaseAuth.uid.toString()).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val user: UserModel? = snapshot.getValue(UserModel::class.java)

                    //For Fix Null Image Crash
                    Glide.with(applicationContext)
                        .load(user?.imageUrl)
                        .fallback(R.drawable.notification_bg_normal_pressed)
                        .fitCenter()
                        .into(binding.imageView)
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@changeImage, "Upload Failed", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun pickImage() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent,100)
    }

    //Use In upload Image To firebase
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

    //Update For Image Only
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

    //Need For Pick Image Upload
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null){
            if (requestCode == 100 && resultCode == Activity.RESULT_OK){ //Acitivity is need if this is fragment
                imageUri = data.data!!
                binding.imageView.setImageURI(imageUri)
                uploadData()
            }
        }

    }
}