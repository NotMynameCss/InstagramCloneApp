package edu.poly.instagramcloneapp.fragment.main


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import com.google.firebase.storage.FirebaseStorage
import edu.poly.instagramcloneapp.adapter.PostAdapter
import edu.poly.instagramcloneapp.activity.person.AccountSettingsActivity
import edu.poly.instagramcloneapp.activity.person.ChangeImage

import edu.poly.instagramcloneapp.databinding.FragmentPersonBinding
import edu.poly.instagramcloneapp.model.UserModel
import edu.poly.instagramcloneapp.model.PostModel

class Person : Fragment() {

    //Chung của Fragment
        private lateinit var binding:FragmentPersonBinding

    //Chung Của Firebase
        private lateinit var database: FirebaseDatabase
        private lateinit var storage: FirebaseStorage
        private lateinit var databaseReference: DatabaseReference
        private lateinit var firebaseAuth: FirebaseAuth
        //Adapter:
        private lateinit var postArrayList: ArrayList<PostModel>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Chung của Person
            binding = FragmentPersonBinding.inflate(layoutInflater)
            firebaseAuth = FirebaseAuth.getInstance()
            storage = FirebaseStorage.getInstance()
            database = FirebaseDatabase.getInstance()


        //View Post Adapter:
            binding.recyclerViewPost.layoutManager = LinearLayoutManager(requireActivity())
            postArrayList = arrayListOf<PostModel>()
            postRetrivie()
        //Action:
            retrivieInfo()

            binding.sumbitProfile.setOnClickListener {
                val intent = Intent(requireActivity(), AccountSettingsActivity::class.java)
                //fix cho fragment not attachted
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
            }
            binding.imagePerson.setOnClickListener{
               val intent = Intent(requireActivity(), ChangeImage::class.java)
                //fix cho fragment not attachted
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
            }

    return binding.root
    }
    private fun retrivieInfo() {
        //Retrive for Info
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.child(firebaseAuth.uid.toString()).addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    val user: UserModel? = snapshot.getValue(UserModel::class.java)
                    binding.namePerson.text = user?.name
                    binding.emailPerson.text = user?.email
                    binding.BioPerson.text = user?.bio
                    //For Fix Null Image Crash
                    Glide.with(requireActivity())
                        .load(user?.imageUrl)
                        .fitCenter()
                        .fallback(R.drawable.notification_bg_normal_pressed)
                        .into(binding.imagePerson)
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }
        )
    }
    private fun postRetrivie() {
        databaseReference = FirebaseDatabase.getInstance().getReference("posts")

        databaseReference.orderByChild("timestamp").addValueEventListener(
            object : ValueEventListener {
                @SuppressLint("SuspiciousIndentation")
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()){
                        postArrayList.clear()
                        for(ds in snapshot.children){

                            val postData = ds.getValue(PostModel::class.java)
                            if (postData?.uid == firebaseAuth.currentUser?.uid)
                                postArrayList.add(postData!!)
                        }
                        postArrayList.reverse()
                        binding.recyclerViewPost.adapter = PostAdapter(requireActivity(),postArrayList)
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            }
        )
    }

}