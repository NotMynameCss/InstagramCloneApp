package edu.poly.instagramcloneapp.activity.person

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import edu.poly.instagramcloneapp.Adapter.postAdapter
import edu.poly.instagramcloneapp.R
import edu.poly.instagramcloneapp.activity.chatActivity
import edu.poly.instagramcloneapp.databinding.ActivityOtherProfileBinding
import edu.poly.instagramcloneapp.model.postModel


//Addfriend: https://www.youtube.com/watch?v=kyq1SQRzfUk&list=PLQFUWT9wUMWEXj9AVanMZg1tRUGr95aBr&index=12&t=76s
class otherProfile : AppCompatActivity() {
    private lateinit var binding: ActivityOtherProfileBinding
    //AddFriends
    private lateinit var requestRef: DatabaseReference
    private lateinit var friendRef: DatabaseReference

    private var CurrentState:String = "Not_Found"
    private lateinit var firebaseAuth: FirebaseAuth
    //Post
    private lateinit var postArrayList: ArrayList<postModel>
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_profile)
        binding = ActivityOtherProfileBinding.inflate(layoutInflater)

        //Phần send message
        val uid:String = intent.getStringExtra("uid").toString()
        val email:String = intent.getStringExtra("email").toString()
        val name:String = intent.getStringExtra("name").toString()
        val imageUrl:String = intent.getStringExtra("image").toString()



        //Phần add friend
        requestRef = FirebaseDatabase.getInstance().getReference("requests")
        firebaseAuth = FirebaseAuth.getInstance()
        friendRef = FirebaseDatabase.getInstance().getReference("Friend")



        //Phần Post:
        binding.recyclerViewPost.layoutManager = LinearLayoutManager(this)
        postArrayList = arrayListOf<postModel>()
        postRetrivie(uid)

        binding.OtherAddFriendBtn.setOnClickListener {
            sendFriendRequest(uid,name,email,imageUrl)
        }

        binding.OtherMessageBtn.setOnClickListener {
            val intent = Intent(this, chatActivity::class.java)
            intent.putExtra("uid",uid)
            intent.putExtra("name",name)
            intent.putExtra("email",email)
            intent.putExtra("image",imageUrl)

            this.startActivity(intent)
        }
        CheckUserExists(uid)
        setContentView(binding.root)
    }

    private fun sendFriendRequest(uid: String, name: String, email: String, imageUrl: String) {
        val user = firebaseAuth.currentUser?.uid.toString()
        if (CurrentState.equals("Not_Found")){
            val hashMap = HashMap<String, String>()
            hashMap.put("status","pending")
            requestRef.child(user).child(uid).updateChildren(
                hashMap as Map<String, Any>
            ).addOnCompleteListener {
                Toast.makeText(this, "Sent Request", Toast.LENGTH_SHORT).show()
                if (it.isSuccessful){

                    Toast.makeText(this, "Wating accept", Toast.LENGTH_SHORT).show()
                    CurrentState = "I_sent_pending"
                    binding.OtherAddFriendBtn.text = "Cancel Friend Request"
                }
                else {
                    Toast.makeText(this, ""+it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
        else if(CurrentState.equals("I_sent_pending") || CurrentState.equals("I_sent_decline")){
            requestRef.child(user).child(uid).removeValue().addOnCompleteListener {
                if (it.isSuccessful){

                    Toast.makeText(this, "You have Cancel", Toast.LENGTH_SHORT).show()
                    CurrentState = "Not_Found"
                    binding.OtherAddFriendBtn.text = "AddFriend"
                }
                else {
                    Toast.makeText(this, ""+it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
        else if(CurrentState.equals("other_send_Pending")){
            requestRef.child(uid).child(user).removeValue().addOnCompleteListener {
                val hashMap = HashMap<String, String>()
                hashMap.put("status","friend")
                hashMap.put("name",name)
                hashMap.put("email",email)
                hashMap.put("imageUrl",imageUrl)

                friendRef.child(user).child(uid).updateChildren(
                    hashMap as Map<String, Any>
                ).addOnCompleteListener {
                    Toast.makeText(this, "Sent Request", Toast.LENGTH_SHORT).show()
                    if (it.isSuccessful){

                        Toast.makeText(this, "You added friend", Toast.LENGTH_SHORT).show()
                        CurrentState = "friend"
                        binding.OtherAddFriendBtn.text = "Unfriend1"
                    }
                    else {
                        Toast.makeText(this, ""+it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        else if(CurrentState.equals("friend")){

        }


    }
    private fun CheckUserExists(uid: String){
        val user = firebaseAuth.currentUser?.uid.toString()
        //Tôi
        friendRef.child(user).child(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    CurrentState = "friend"
                    binding.OtherAddFriendBtn.text = "Unfriend2"
                    binding.declineBtn.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
        friendRef.child(uid).child(user).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    CurrentState = "friend"
                    binding.OtherAddFriendBtn.text = "Unfriend3"
                    binding.declineBtn.visibility = View.GONE

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        requestRef.child(user).child(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("status").getValue().toString().equals("pending")){
                        CurrentState = "I_sent_pending"
                        binding.OtherAddFriendBtn.text = "Cancel Friend Request"
                        binding.declineBtn.visibility = View.GONE

                    }
                    else if (snapshot.child("status").getValue().toString().equals("declined")){
                        CurrentState = "I_sent_decline"
                        binding.OtherAddFriendBtn.text = "Cancel Friend Request"
                        binding.declineBtn.visibility = View.GONE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        requestRef.child(uid).child(user).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("status").getValue().toString().equals("pending")){
                        CurrentState = "other_send_Pending"
                        binding.OtherAddFriendBtn.text = "Accpect Friend"
                        binding.declineBtn.text = "Decline Friend"
                        binding.declineBtn.visibility = View.VISIBLE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        if (CurrentState.equals("Not_Found")){
            CurrentState = "Not_Found"
            binding.OtherAddFriendBtn.text = "Add Friend"
            binding.declineBtn.visibility = View.GONE


        }
    }
    private fun postRetrivie(uid: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("posts")

        databaseReference.orderByChild("timestamp").addValueEventListener(
            object : ValueEventListener {
                @SuppressLint("SuspiciousIndentation")
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()){
                        postArrayList.clear()
                        for(ds in snapshot.children){

                            val postData = ds.getValue(postModel::class.java)
                            if (postData?.uid == uid)
                                postArrayList.add(postData!!)
                        }
                        postArrayList.reverse()
                        binding.recyclerViewPost.adapter = postAdapter(this@otherProfile,postArrayList)
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            }
        )
    }
}