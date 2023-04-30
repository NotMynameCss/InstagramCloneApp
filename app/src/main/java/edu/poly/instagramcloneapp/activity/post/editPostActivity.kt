package edu.poly.instagramcloneapp.activity.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.poly.instagramcloneapp.databinding.ActivityEditPostAdapterBinding
import edu.poly.instagramcloneapp.model.UserModel
import edu.poly.instagramcloneapp.model.postModel
import java.text.SimpleDateFormat
import java.util.*

class editPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPostAdapterBinding
    private var formater = SimpleDateFormat("yyyy.MM.dd 'at' hh:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPostAdapterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uid = intent.getStringExtra("uid").toString()
        val email = intent.getStringExtra("email").toString()
        val content = intent.getStringExtra("content").toString()
        val timestamp = intent.getStringExtra("timestamp").toString()


        binding.userNameSearch.text = email

        binding.postSaveCommentBtn.setOnClickListener {
            post_edit()
        }
        binding.closeBtn.setOnClickListener {
            finish()
        }
    }
    private fun post_edit() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        if (binding.description.text.isEmpty()) {
            Toast.makeText(this, "can't send empty", Toast.LENGTH_SHORT).show()
        } else {
            //Generate Information for sender
            val post_Add = postModel(
                uid = firebaseAuth.currentUser?.uid.toString(),
                email = firebaseAuth.currentUser?.email.toString(),
                content = binding.description.text.toString(),
                timestamp = formater.format(Date())
            )
            val randomKey = database.reference.push().key
            val editMap = mapOf(
                "uid" to post_Add.uid,
                "email" to post_Add.email,
                "content" to post_Add.content,
                "timestamp" to post_Add.timestamp
            )
            database.reference.child("posts")
                .child(randomKey.toString()).updateChildren(editMap).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this, "Edit Success", Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this, ""+it.exception, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}