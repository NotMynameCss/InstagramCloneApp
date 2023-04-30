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
//Unfriend Friend: https://www.youtube.com/watch?v=Tk3SDs7g_Ik&list=PLYx38U7gxBf0PyWzqW9JEv034wBlBDl8z&index=34
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
        friendRef = FirebaseDatabase.getInstance().getReference("friend")
        val user = firebaseAuth.currentUser?.uid.toString()


        //Phần Post:
        binding.recyclerViewPost.layoutManager = LinearLayoutManager(this)
        postArrayList = arrayListOf<postModel>()
        postRetrivie(uid)

        binding.OtherMessageBtn.setOnClickListener {
            val intent = Intent(this, chatActivity::class.java)
            intent.putExtra("uid",uid)
            intent.putExtra("name",name)
            intent.putExtra("email",email)
            intent.putExtra("image",imageUrl)

            this.startActivity(intent)
        }
        binding.OtherAddFriendBtn.setOnClickListener {
            sendFriendRequest(uid, name, email, imageUrl, user)
        }
        binding.declineBtn.setOnClickListener {
            unFriend(uid,user)
        }

        CheckUserExists(uid,user)
        setContentView(binding.root)
    }

    @SuppressLint("SetTextI18n")
    private fun sendFriendRequest(uid: String, name: String, email: String, imageUrl: String, user: String
    ) {
        //Ok Gửi
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
                    binding.declineBtn.text = "Cancel 1"
                    binding.OtherAddFriendBtn.visibility = View.GONE
                    binding.declineBtn.visibility = View.VISIBLE
                }
                else {
                    Toast.makeText(this, ""+it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
        //Ok Xóa gửi
        else if(CurrentState.equals("I_sent_pending")){
                    declineFriendTest(uid,user)
                    Toast.makeText(this, "You have Cancel", Toast.LENGTH_SHORT).show()
                    CurrentState = "Not_Found"
                    binding.OtherAddFriendBtn.text = "Add Friend 1"
                    binding.declineBtn.visibility = View.GONE
        }
        //Ok đã kết bạn
        else if(CurrentState.equals("other_send_Pending")){
            requestRef.child(uid).child(user).removeValue().addOnCompleteListener {
                if (it.isSuccessful){
                    val hashMap = HashMap<String, String>()
                    hashMap.put("status","friend")
                    hashMap.put("uid",user)
                    hashMap.put("name",name)
                    hashMap.put("email",email)
                    hashMap.put("imageUrl",imageUrl)

                    friendRef.child(user).child(uid).updateChildren(
                        hashMap as Map<String, Any>
                    ).addOnCompleteListener {
                        Toast.makeText(this, "Sent Request", Toast.LENGTH_SHORT).show()
                        if (it.isSuccessful){
                            if (CurrentState.equals("friend")){
                                Toast.makeText(this, "You added friend", Toast.LENGTH_SHORT).show()

                                binding.OtherAddFriendBtn.visibility = View.GONE
                                binding.declineBtn.text = "Unfriend 1"
                                binding.declineBtn.visibility = View.VISIBLE
                            }
                        }
                        else {
                            Toast.makeText(this, ""+it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
    private fun CheckUserExists(uid: String, user: String){
    //Đã Kết bạn
        //Tôi,Ok
        friendRef.child(user).child(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                        CurrentState = "friend"
                        binding.declineBtn.text = "Unfriend 2"
                        binding.OtherAddFriendBtn.visibility = View.GONE
                        binding.declineBtn.visibility = View.VISIBLE
                }else{
                    CurrentState = "Not_Found"
                    binding.OtherAddFriendBtn.visibility = View.VISIBLE
                    binding.declineBtn.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
        //Bạn,Ok
        friendRef.child(uid).child(user).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                        CurrentState = "friend"
                        binding.OtherAddFriendBtn.visibility = View.GONE
                        binding.declineBtn.text = "Unfriend 3"
                        binding.declineBtn.visibility = View.VISIBLE

                }else{
                    CurrentState = "Not_Found"
                    binding.OtherAddFriendBtn.visibility = View.VISIBLE
                    binding.declineBtn.visibility = View.GONE

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        //Chưa Kết Bạn
        //Tôi,Ok
        requestRef.child(user).child(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    //Ok
                    if (snapshot.child("status").getValue().toString().equals("pending")){
                        CurrentState = "I_sent_pending"
                    }
                }else{
                    CurrentState = "Not_Found"
                    binding.OtherAddFriendBtn.visibility = View.VISIBLE
                    binding.declineBtn.visibility = View.GONE

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        //Bạn,Ok
        requestRef.child(uid).child(user).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("status").getValue().toString().equals("pending")){
                        CurrentState = "other_send_Pending"
                        binding.OtherAddFriendBtn.text = "Acpect Friend"
                        binding.OtherAddFriendBtn.visibility = View.VISIBLE
                        binding.declineBtn.text = "Decline Friend"
                        binding.declineBtn.visibility = View.VISIBLE
                    }
                }else{
                    CurrentState = "Not_Found"
                    binding.OtherAddFriendBtn.text = "Add Friend"
                    binding.OtherAddFriendBtn.visibility = View.VISIBLE
                    binding.declineBtn.visibility = View.GONE

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }
    private fun unFriend(uid: String, user: String){
        if (CurrentState.equals("friend")){
            friendRef.child(uid).child(user).removeValue()
            friendRef.child(user).child(uid).removeValue()

                    Toast.makeText(this, "Unfriend Successfull", Toast.LENGTH_SHORT).show()
                    CurrentState = "Not_Found"
                    binding.OtherAddFriendBtn.text = "Add Friend 4"
                    binding.OtherAddFriendBtn.visibility = View.VISIBLE
                    binding.declineBtn.visibility = View.GONE
        }
        else if (CurrentState.equals("other_send_Pending")){
                    declineFriendTest(uid,user)
                    CurrentState = "Not_Found"
                    binding.OtherAddFriendBtn.text = "Add Friend 5"
                    binding.OtherAddFriendBtn.visibility = View.VISIBLE
                    binding.declineBtn.visibility = View.GONE
        }
        else if (CurrentState.equals("I_sent_pending")){
            declineFriendTest(uid,user)
            CurrentState = "Not_Found"
            binding.OtherAddFriendBtn.text = "Add Friend 6"
            binding.OtherAddFriendBtn.visibility = View.VISIBLE
            binding.declineBtn.visibility = View.GONE
        }
    }

    private fun declineFriendTest(uid: String, user: String){
            requestRef.child(user).child(uid).removeValue()
            requestRef.child(uid).child(user).removeValue()
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