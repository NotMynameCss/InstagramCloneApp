package edu.poly.instagramcloneapp.activity.person

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import edu.poly.instagramcloneapp.activity.login.login
import edu.poly.instagramcloneapp.databinding.ActivityAccountSettingsBinding
import edu.poly.instagramcloneapp.model.UserModel

class AccountSettingsActivity : AppCompatActivity() {
    //Chung của Fragment
        private lateinit var binding: ActivityAccountSettingsBinding

    //Chung Của Firebase
        private lateinit var database: FirebaseDatabase
        private lateinit var storage: FirebaseStorage
        private lateinit var databaseReference: DatabaseReference
        private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Chung của Person
            binding = ActivityAccountSettingsBinding.inflate(layoutInflater)

            firebaseAuth = FirebaseAuth.getInstance()
            storage = FirebaseStorage.getInstance()
            database = FirebaseDatabase.getInstance()

        //Action:
            binding.closeEditBtn.setOnClickListener {
                finish()
            }
            //Upload Profile Info
            binding.updateBtn.setOnClickListener {
                if(binding.editNamePersonText.text!!.isEmpty()){
                    Toast.makeText(this, "F1", Toast.LENGTH_SHORT).show()
                }else{
                    editInfo()
                }
            }
            binding.deleteAccountBtn.setOnClickListener{
                deleteAccount()
            }
        //Very Need For This acitivity:
        setContentView(binding.root)
    }
    private fun editInfo() {
        //Chung CHo Update
        databaseReference = Firebase.database.getReference("users")

        val user = UserModel(
            name = binding.editNamePersonText.text.toString(),
            bio = binding.editBioPersonText.text.toString(),
        )
        //Tạo dữ liệu Sẽ Update
        val editMap = mapOf(
            "name" to user.name,
            "bio" to user.bio,
        )
        val userId = firebaseAuth.uid
        if (userId != null) {
            databaseReference.child(userId).updateChildren(editMap)
            finish()
        }
    }
    private fun deleteAccount() {
        val user = Firebase.auth.currentUser
        databaseReference = Firebase.database.getReference("users")
        databaseReference.child(firebaseAuth.uid.toString()).removeValue()

        user?.delete()?.addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(this, "Deleted Successfull", Toast.LENGTH_SHORT).show()
                //because deleteAccount can't signOut so Start new Activity
                val intent = Intent(this,login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
                finish()
            }else{
                Log.e("error: ",it.exception.toString())
            }
        }
    }
}