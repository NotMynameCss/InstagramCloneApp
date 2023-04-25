package edu.poly.instagramcloneapp



//Multi Image:
//+https://stackoverflow.com/questions/46272309/upload-multiple-images-to-firebase-storage
//+https://sl.bing.net/kgkyaLuz7vM

import android.Manifest
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
import edu.poly.instagramcloneapp.Adapter.MessageAdapter
import edu.poly.instagramcloneapp.databinding.ActivityChatBinding
import edu.poly.instagramcloneapp.model.MessageModel
import gun0912.tedbottompicker.TedBottomPicker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


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

    //For PickMultiple Image
    private lateinit var storage: FirebaseStorage
    private lateinit var databaseReference: DatabaseReference
    private lateinit var StorageReference: StorageReference
    private var selectedUriList: ArrayList<Uri> = ArrayList()
    private var urlString: ArrayList<Uri> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        senderUid = FirebaseAuth.getInstance().uid.toString()
        receiverUid = intent.getStringExtra("uid").toString()

        list = ArrayList()

        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid + senderUid

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()



        binding.name.setText(intent.getStringExtra("name").toString())

        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.attachment.setOnClickListener {

        }

        binding.camera.setOnClickListener {
            requestPermission()
            /////


            //////
            }

        binding.sendBtn.setOnClickListener {
            sendMessage()
        }

        readMessage()
    }
//For Chat message
    private fun sendMessage(){
        val formater = SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss a zzz")
        if (binding.messageText.text.isEmpty()) {
            Toast.makeText(this, "can't send empty", Toast.LENGTH_SHORT).show()
        } else {
            val message = MessageModel(
                binding.messageText.text.toString(),
                senderUid,
                formater.format(Date())
            )
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

    private fun readMessage() {
        database.reference.child("chats").child(senderRoom).child("message")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()

                    for (ds in snapshot.children) {
                        val data = ds.getValue(MessageModel::class.java)
                        list.add(data!!)
                    }

                    binding.messageLayout.adapter = MessageAdapter(this@chatActivity, list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@chatActivity, "F4", Toast.LENGTH_SHORT).show()
                }

            })
    }

//For Upload Multiple Image
    //Make Request Image
    private fun requestPermission() {
        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                openBottomPicker()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    this@chatActivity,
                    "Permission Denied\n$deniedPermissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        TedPermission.create()
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .check();
    }

    //For Pick Image
    private fun openBottomPicker() {

        TedBottomPicker.with(this@chatActivity)
            .setPeekHeight(1600)
            .showTitle(false)
            .setCompleteButtonText("Done")
            .setEmptySelectionText("No Select")
            .setSelectedUriList(selectedUriList)
            .showMultiImage { uriList ->
                // here is selected image uri list
                selectedUriList = uriList as ArrayList<Uri>
                doneUpload2(selectedUriList)
            }


    }

    private fun doneUpload2(selectedUriList: ArrayList<Uri>) {
        val uri = selectedUriList


        StorageReference = storage.getReference("photos")
        for (i in this.selectedUriList.indices) {
            uri[i] = Uri.parse("file://" + this.selectedUriList[i].path)
            val ref: StorageReference = StorageReference.child(uri[i]?.getLastPathSegment().toString())
            ref.putFile(uri[i]).addOnSuccessListener(this){


                ref.downloadUrl.addOnCompleteListener {
                    task->
                    if (task.isSuccessful) {
                        //result : format link imageUpload
                        val content = task.result
                        urlString.add(content)
                        if (urlString.size == selectedUriList.size){
                            storeLink(urlString)
                        }
                    }
                    else {
                        // Handle failures
                        // ...
                            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
                }
            }

            }
    private fun storeLink(urlString: ArrayList<Uri>) {
        if (urlString.size > 0) {
            val hashMap = HashMap<String, String>()
            for (i in urlString.indices) {
                hashMap["ImgLink$i"] = urlString[i].toString()
            }
            val databaseReference =
                FirebaseDatabase.getInstance().reference.child("User")
            databaseReference.push().setValue(hashMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Successfully Uplosded",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(
                        this,
                        "" + e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            this.selectedUriList.clear()
        }
    }

}

