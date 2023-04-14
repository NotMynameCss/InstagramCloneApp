package edu.poly.instagramcloneapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import edu.poly.instagramcloneapp.Adapter.MessageAdapter
import edu.poly.instagramcloneapp.databinding.ActivityChatBinding
import edu.poly.instagramcloneapp.model.MessageModel
import java.text.SimpleDateFormat
import java.util.Date



//Chat Send Message: https://www.youtube.com/watch?v=mycu5zAoox0&t=1s
class chatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding

    private lateinit var database: FirebaseDatabase

    //Chat
    private lateinit var senderUid: String
    private lateinit var receiverUid: String

    private lateinit var senderRoom: String
    private lateinit var receiverRoom: String
    //Message
    private lateinit var list: ArrayList<MessageModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        senderUid = FirebaseAuth.getInstance().uid.toString()
        receiverUid = intent.getStringExtra("uid").toString()

        list = ArrayList()


        senderRoom = senderUid+receiverUid
        receiverRoom = receiverUid+senderUid



        database = FirebaseDatabase.getInstance()

        val formater = SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss a zzz")


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

        database.reference.child("chats").child(senderRoom).child("message")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()

                    for (ds in snapshot.children){
                        val data = ds.getValue(MessageModel::class.java)
                        list.add(data!!)
                    }

                    binding.messageLayout.adapter = MessageAdapter(this@chatActivity,list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@chatActivity, "F4", Toast.LENGTH_SHORT).show()
                }

            })
    }
}