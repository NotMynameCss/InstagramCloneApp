package edu.poly.instagramcloneapp.activity.post

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import edu.poly.instagramcloneapp.databinding.ActivityEditPostAdapterBinding
import edu.poly.instagramcloneapp.model.PostModel
import java.text.SimpleDateFormat
import java.util.*

class EditPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPostAdapterBinding
    @SuppressLint("SimpleDateFormat")
    private var timeformater = SimpleDateFormat("yyyy.MM.dd 'at' hh:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPostAdapterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uid = intent.getStringExtra("uid").toString()
        val email = intent.getStringExtra("email").toString()
        val content = intent.getStringExtra("content").toString()
        val timestamp = intent.getStringExtra("timestamp").toString()
        val image = intent.getStringExtra("image").toString()

        binding.userNameSearch.text = email


        binding.postSaveCommentBtn.setOnClickListener {
            postedit()
        }
        binding.closeBtn.setOnClickListener {
            finish()
        }
    }
    private fun postedit() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        if (binding.description.text.isEmpty()) {
            Toast.makeText(this, "can't send empty", Toast.LENGTH_SHORT).show()
        } else {
            //Generate Information for sender
            val postEdit = PostModel(
                uid = firebaseAuth.currentUser?.uid.toString(),
                email = firebaseAuth.currentUser?.email.toString(),
                content = binding.description.text.toString(),
                timestamp = timeformater.format(Date())
            )
            val randomKey = database.reference.push().key
            val editMap = mapOf(
                "uid" to postEdit.uid,
                "email" to postEdit.email,
                "content" to postEdit.content,
                "timestamp" to postEdit.timestamp
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