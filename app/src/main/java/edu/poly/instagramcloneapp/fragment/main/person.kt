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
import edu.poly.instagramcloneapp.Adapter.postAdapter
import edu.poly.instagramcloneapp.activity.person.accountSettingsActivity
import edu.poly.instagramcloneapp.activity.person.changeImage

import edu.poly.instagramcloneapp.databinding.FragmentPersonBinding
import edu.poly.instagramcloneapp.model.UserModel
import edu.poly.instagramcloneapp.model.postModel


//Get Username,emai from currentUser: https://stackoverflow.com/questions/59117267/getting-currently-logged-in-users-id-in-firebase-auth-in-kotlin

//Read Data: https://www.youtube.com/watch?v=_DtbxQ9EEhc&t=812s

//* Update can alter Create
class person : Fragment() {

    //Chung của Fragment
        private lateinit var binding:FragmentPersonBinding

    //Chung Của Firebase
        private lateinit var database: FirebaseDatabase
        private lateinit var storage: FirebaseStorage
        private lateinit var databaseReference: DatabaseReference

        //Firebase Auth: ACcount
        private lateinit var firebaseAuth: FirebaseAuth
        //Adapter:
        private lateinit var postArrayList: ArrayList<postModel>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Chung của Person
            binding = FragmentPersonBinding.inflate(layoutInflater)
            firebaseAuth = FirebaseAuth.getInstance()
            storage = FirebaseStorage.getInstance()
            database = FirebaseDatabase.getInstance()


        //Adapter View
        binding.recyclerViewPost.layoutManager = LinearLayoutManager(requireActivity())
        postArrayList = arrayListOf<postModel>()

        //Retrive for Info
        retrivieInfo()

        postRetrivie()
        binding.sumbitProfile.setOnClickListener {
            val intent = Intent(requireActivity(), accountSettingsActivity::class.java)
            //fix cho fragment not attachted
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }
        //Select Image
            binding.imagePerson.setOnClickListener{
               val intent = Intent(requireActivity(), changeImage::class.java)
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

                            val postData = ds.getValue(postModel::class.java)
                            if (postData?.uid == firebaseAuth.currentUser?.uid)
                                postArrayList.add(postData!!)
                        }
                        postArrayList.reverse()
                        binding.recyclerViewPost.adapter = postAdapter(requireActivity(),postArrayList)
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            }
        )
    }

}