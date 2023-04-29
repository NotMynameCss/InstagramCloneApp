package edu.poly.instagramcloneapp.activity.person

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import edu.poly.instagramcloneapp.databinding.ActivityAccountSettingsBinding
import edu.poly.instagramcloneapp.databinding.FragmentPersonBinding
import edu.poly.instagramcloneapp.model.UserModel


//(Use Create) Upload Profile: https://www.youtube.com/watch?v=UDgMEmqEybc&list=PPSV
//(Use update) Update Data Firebase: https://www.youtube.com/watch?v=srQ0Nq3mJ_M

class accountSettingsActivity : AppCompatActivity() {
    //Chung của Fragment
    private lateinit var binding: ActivityAccountSettingsBinding

    //Chung Của Firebase
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountSettingsBinding.inflate(layoutInflater)

        //Chung của Person
        binding = ActivityAccountSettingsBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()

        //binding
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
        //For Binding Btn work
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
}