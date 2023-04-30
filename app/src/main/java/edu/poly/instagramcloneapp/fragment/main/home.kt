package edu.poly.instagramcloneapp.fragment.main


//Nguồn tham khảo:
    // Connect firebase:  https://www.youtube.com/watch?v=YOT8P1PtJQg&t=618s
    //Viewbinding in fragment: https://www.youtube.com/watch?v=v11x54y5YVc
    //SignIn and SignOut : https://www.youtube.com/watch?v=idbxxkF1l6k&list=PLQFUWT9wUMWEXj9AVanMZg1tRUGr95aBr&index=10&t=784s
    //Upload ảnh: https://www.youtube.com/watch?v=GmpD2DqQYVk

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*

import edu.poly.instagramcloneapp.Adapter.postAdapter
import edu.poly.instagramcloneapp.activity.post.addPostActivity

import edu.poly.instagramcloneapp.databinding.FragmentHomeBinding
import edu.poly.instagramcloneapp.model.postModel


class home : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    //Firbase Image

    //Firebase:
        private lateinit var databaseReference: DatabaseReference

    //Adapter:
        private lateinit var postArrayList: ArrayList<postModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Chung của Home
            binding = FragmentHomeBinding.inflate(layoutInflater)


        binding.recyclerViewPost.layoutManager = LinearLayoutManager(requireActivity())

        postArrayList = arrayListOf<postModel>()

        recylerRetrivie()

        binding.postText.setOnClickListener {
            val intent = Intent(requireActivity(), addPostActivity::class.java)
            startActivity(intent)
        }
        //Cần cho onCreateView()
            return binding.root
    }

    private fun recylerRetrivie() {
        databaseReference = FirebaseDatabase.getInstance().getReference("posts")

        databaseReference.orderByChild("timestamp").addValueEventListener(
            object : ValueEventListener {
                @SuppressLint("SuspiciousIndentation")
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()){
                        postArrayList.clear()
                        for(ds in snapshot.children){
                            val postData = ds.getValue(postModel::class.java)
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