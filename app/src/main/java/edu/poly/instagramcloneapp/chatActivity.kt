package edu.poly.instagramcloneapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import edu.poly.instagramcloneapp.databinding.ActivityChatBinding
import edu.poly.instagramcloneapp.databinding.ActivityMainBinding
import edu.poly.instagramcloneapp.model.MessageModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date



//Chat Send Message: https://www.youtube.com/watch?v=mycu5zAoox0&t=1s
class chatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private var firebaseUser: FirebaseUser? = null
    private var databaseReference: DatabaseReference? = null
    private lateinit var database: FirebaseDatabase

    private lateinit var senderUid: String
    private lateinit var receiverUid: String

    private lateinit var senderRoom: String
    private lateinit var receiverRoom: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        senderUid = FirebaseAuth.getInstance().uid.toString()
        receiverUid = intent.getStringExtra("userId").toString()

        senderRoom = senderUid+receiverUid
        receiverRoom = receiverUid+senderUid

        database = FirebaseDatabase.getInstance()

        val formater = SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss a zzz")
        val date = Date()

        binding.sendBtn.setOnClickListener {
            if (binding.messageText.text.isEmpty()){
                Toast.makeText(this, "can't send empty", Toast.LENGTH_SHORT).show()
            }else{
                val message = MessageModel(binding.messageText.text.toString(),senderUid,formater.format(Date()))
                val randomKey = database.reference.push().key

                database.reference.child("chats")
                    .child(senderRoom).child("message")
                    .child(randomKey.toString()).setValue(message)
                    .addOnSuccessListener {
                        database.reference.child("chats")
                            .child(receiverRoom)
                            .child("message")
                            .child(randomKey.toString()).setValue(message)
                            .addOnSuccessListener {
                                binding.messageText.text = null
                                Toast.makeText(this, "Sent", Toast.LENGTH_SHORT).show()
                            }
                    }
            }
        }
    }
}