package edu.poly.instagramcloneapp.activity

//Introduce: Use for interact with button

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import edu.poly.instagramcloneapp.adapter.MessageAdapter
import edu.poly.instagramcloneapp.databinding.ActivityChatBinding
import edu.poly.instagramcloneapp.model.MessageModel
import gun0912.tedbottompicker.TedBottomPicker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Chatactivity : AppCompatActivity() {
    //Chung:
        private lateinit var binding: ActivityChatBinding
        private lateinit var database: FirebaseDatabase
        private lateinit var storage: FirebaseStorage
        private lateinit var storagereference: StorageReference
    //Need for Create Chat Room:
        private lateinit var senderUid: String
        private lateinit var receiverUid: String
        private lateinit var senderRoom: String
        private lateinit var receiverRoom: String

    //Message_list
        private lateinit var messagelist: ArrayList<MessageModel>
    //For PickMultiple Image
        private var selectedImageUriList: ArrayList<Uri> = ArrayList()
        private var urlImageString: ArrayList<Uri> = ArrayList()
        @SuppressLint("SimpleDateFormat")
        private var timeformater = SimpleDateFormat("yyyy.MM.dd 'at' hh:mm")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Chung
            //Need for UI:
                binding = ActivityChatBinding.inflate(layoutInflater)
                setContentView(binding.root)
            //Other:
                database = FirebaseDatabase.getInstance()
                storage = FirebaseStorage.getInstance()




        //Room chat need:
            senderUid = FirebaseAuth.getInstance().uid.toString()
            receiverUid = intent.getStringExtra("uid").toString()
            senderRoom = senderUid + receiverUid
            receiverRoom = receiverUid + senderUid

            messagelist = ArrayList()
        //Action:
            binding.name.text = intent.getStringExtra("email").toString()

            binding.sendBtn.setOnClickListener {
                sendTextMessage()
            }
            binding.camera.setOnClickListener {
                requestImagePermission()
            }
            binding.backBtn.setOnClickListener {
                finish()
            }
            readMessage()
    }
//For Chat message(Ok)
    private fun sendTextMessage(){
        if (binding.messageText.text.isEmpty()) {
            Toast.makeText(this, "can't send empty", Toast.LENGTH_SHORT).show()
        } else {
            //Need:
                val message = MessageModel(
                    message = binding.messageText.text.toString(),
                    senderId = senderUid,
                    receiverId = receiverUid,
                    timestamp = timeformater.format(Date())
                )
                val randomKey = database.reference.push().key
            //Action: send Text to both reader and receiver
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
    private fun readMessage() {
        database.reference.child("chats").child(senderRoom).child("message")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messagelist.clear()
                    for (ds in snapshot.children) {
                        val data = ds.getValue(MessageModel::class.java)
                        if (data != null) {
                            messagelist.add(data)
                        }
                    }
                    binding.messageLayout.adapter = MessageAdapter(this@Chatactivity, messagelist)
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@Chatactivity, "F4", Toast.LENGTH_SHORT).show()
                }
            })
    }
//For Upload Multiple Image (During Fixing)

    private fun requestImagePermission() {
        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                openImageBottomPicker()
            }
            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    this@Chatactivity,
                    "Permission Denied\n$deniedPermissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        //This TedPermission come from Open source
        TedPermission.create()
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .check()
    }
    //For Pick Image
    private fun openImageBottomPicker() {
        this.selectedImageUriList.clear()
        TedBottomPicker.with(this@Chatactivity)
            .setPeekHeight(1600)
            .showTitle(false)
            .setCompleteButtonText("Done")
            .setEmptySelectionText("No Select")
            .setSelectedUriList(selectedImageUriList)
            .showMultiImage { uriList ->
                //After Pick Image from bottom, pass to next Function:
                selectedImageUriList = uriList as ArrayList<Uri>
                uploadMultipleImage(selectedImageUriList)
            }
    }
    private fun uploadMultipleImage(selectedImageUriList: ArrayList<Uri>) {
        val uri = selectedImageUriList
        storagereference = storage.getReference("photos")
        for (i in this.selectedImageUriList.indices) {

            //Tạo tên ảnh cho FIREBASE_STORAGE
            uri[i] = Uri.parse("file://" + this.selectedImageUriList[i].path)
            val ref: StorageReference = storagereference.child(uri[i].lastPathSegment.toString())

            //putFile: Gửi ảnh lên FIREBASE_STORAGE
            ref.putFile(uri[i]).addOnSuccessListener(this){
                //downloadUrl: tạo Link Kết nối ảnh trên FIREBASE_STORAGE(Vì putfile chỉ dùng để up chứ tạo link ảnh)
                ref.downloadUrl.addOnCompleteListener {
                    task->
                    if (task.isSuccessful) {
                        //result : Hold Link to image
                        val content = task.result
                        urlImageString.add(content)
                        if (urlImageString.size == selectedImageUriList.size){
                            sendImageMessage(urlImageString)
                        }
                    }
                    else {
                        Toast.makeText(this, "Failed Generated Link Image", Toast.LENGTH_SHORT).show()
                    }
                }
                }
            }
    }
    private fun sendImageMessage(urlImageString: ArrayList<Uri>) {
            //Need:
                val randomKey = database.reference.push().key
                if (urlImageString.size > 0) {
                    val hashMap = HashMap<String, String>()
                    for (i in urlImageString.indices) {
                        hashMap[i.toString()] = urlImageString[i].toString()
                    }
                    val imageMessage = MessageModel(
                        Image = hashMap.toString(),
                        senderId = senderUid,
                        receiverId = receiverUid,
                        timestamp = timeformater.format(Date())
                    )
            //Action: send Image to both reader and receiver
                database.reference.child("chats")
                    .child(senderRoom).child("message")
                    .child(randomKey.toString())
                    .setValue(imageMessage)
                    .addOnSuccessListener {
                        database.reference.child("chats")
                            .child(receiverRoom)
                            .child("message")
                            .child(randomKey.toString()).setValue(imageMessage)
                            .addOnSuccessListener{
                                binding.messageText.text = null
                                Toast.makeText(this, "Sent Image Success", Toast.LENGTH_SHORT).show()
                            }
                    }.addOnFailureListener{ e ->
                        Toast.makeText(
                            this,
                            "" + e.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        //clear urlImageString to allow user send another Image
        this.urlImageString.clear()
    }
}

