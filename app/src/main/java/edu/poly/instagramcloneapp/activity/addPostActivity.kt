package edu.poly.instagramcloneapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import edu.poly.instagramcloneapp.databinding.ActivityAddPostBinding
import edu.poly.instagramcloneapp.model.postModel
import java.text.SimpleDateFormat
import java.util.*

class addPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPostBinding
    private var formater = SimpleDateFormat("yyyy.MM.dd 'at' hh:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveNewPostBtn.setOnClickListener {
            post_add()
        }
        binding.closeAddPostBtn.setOnClickListener {
            finish()
        }
    }
    private fun post_add() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        if (binding.descriptionPost.text.isEmpty()) {
            Toast.makeText(this, "can't send empty", Toast.LENGTH_SHORT).show()
        } else {
            //Generate Information for sender
            val post_Add = postModel(
                uid = firebaseAuth.currentUser?.uid.toString(),
                email = firebaseAuth.currentUser?.email.toString(),
                content = binding.descriptionPost.text.toString(),
                timestamp = formater.format(Date())
            )
            val randomKey = database.reference.push().key

            database.reference.child("posts")
                .child(randomKey.toString()).setValue(post_Add)
                .addOnSuccessListener{
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    finish()
                }
        }
    }
}