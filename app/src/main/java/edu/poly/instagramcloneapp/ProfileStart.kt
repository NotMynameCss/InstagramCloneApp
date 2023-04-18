package edu.poly.instagramcloneapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.poly.instagramcloneapp.databinding.ActivityMainBinding
import edu.poly.instagramcloneapp.databinding.ActivityProfileStartBinding
import edu.poly.instagramcloneapp.model.UserModel

class ProfileStart : AppCompatActivity() {
    private lateinit var binding: ActivityProfileStartBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        firebaseAuth = FirebaseAuth.getInstance()


        binding = ActivityProfileStartBinding.inflate(layoutInflater)
        uploadInfo()

        setContentView(binding.root)
    }
    private fun uploadInfo() {
        //Chung CHo Update
        databaseReference = Firebase.database.getReference("users")


        val user = UserModel(
            uid = firebaseAuth.uid.toString(),
            name = binding.namePerson3.text.toString(),
            email = firebaseAuth.currentUser?.email,
        )

        //Tạo dữ liệu Sẽ Update
        val editMap = mapOf(
            "uid" to user.uid,
            "name" to user.name,
            "email" to user.email,
        )
        val userId = firebaseAuth.uid
        if (userId != null) {
            if (user.name.isNullOrEmpty()){
                Toast.makeText(this, "Plz Enter Name", Toast.LENGTH_SHORT).show()
            }else{
                databaseReference.child(userId).setValue(editMap)
            }
        }

    }
}