package edu.poly.instagramcloneapp.activity.post

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import edu.poly.instagramcloneapp.databinding.ActivityAddPostBinding
import edu.poly.instagramcloneapp.model.PostModel
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class AddPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPostBinding
    @SuppressLint("SimpleDateFormat")
    private var timeformater = SimpleDateFormat("yyyy.MM.dd 'at' hh:mm")
    //Chung Của Firebase
    private lateinit var storage: FirebaseStorage
    //For SelectImage
    private lateinit var imageUri: Uri
    //Firebase Auth: Account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        storage = FirebaseStorage.getInstance()


        setContentView(binding.root)

        binding.imagePost.setOnClickListener {
            pickImage()
        }
        binding.saveNewPostBtn.setOnClickListener {
            uploadData()
        }
        binding.closeAddPostBtn.setOnClickListener {
            finish()
        }
    }

    private fun pickImage() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent,100)
    }

    //Use In upload Image To firebaseStorage
    private fun uploadData() {

        val uriName = Uri.parse("file://" + this.imageUri.path)
        val reference = storage.reference.child("Post").child(uriName.lastPathSegment.toString())
        reference.putFile(imageUri).addOnCompleteListener {
            if (it.isSuccessful){
                //downloadUrl: tạo link ảnh trên firebase
                reference.downloadUrl.addOnSuccessListener { task ->
                    postadd(task.toString())
                }
            }
        }
    }
    private fun postadd(imgUrl: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        if (binding.descriptionPost.text.isEmpty()) {
            Toast.makeText(this, "can't send empty", Toast.LENGTH_SHORT).show()
        } else {
            //Generate Information for sender
            val postAdd = PostModel(
                uid = firebaseAuth.currentUser?.uid.toString(),
                email = firebaseAuth.currentUser?.email.toString(),
                content = binding.descriptionPost.text.toString(),
                image = imgUrl,
                timestamp = timeformater.format(Date())
            )
            val randomKey = database.reference.push().key

            database.reference.child("posts")
                .child(randomKey.toString()).setValue(postAdd)
                .addOnSuccessListener{
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    finish()
                }
        }
    }

    //Need For Pick Image Upload
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null){
            if (requestCode == 100 && resultCode == Activity.RESULT_OK){ //Acitivity is need if this is fragment
                imageUri = data.data!!
                binding.imagePost.setImageURI(imageUri)
                uploadData()
            }
        }

    }
}
