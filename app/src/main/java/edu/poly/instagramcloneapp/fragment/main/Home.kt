package edu.poly.instagramcloneapp.fragment.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*

import edu.poly.instagramcloneapp.adapter.PostAdapter
import edu.poly.instagramcloneapp.activity.post.AddPostActivity

import edu.poly.instagramcloneapp.databinding.FragmentHomeBinding
import edu.poly.instagramcloneapp.model.PostModel

class Home : Fragment() {
    //Chung:
    private lateinit var binding: FragmentHomeBinding
    //Firebase:
        private lateinit var databaseReference: DatabaseReference
    //Adapter:
        private lateinit var postArrayList: ArrayList<PostModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Chung của Home
            binding = FragmentHomeBinding.inflate(layoutInflater)

        //Adapter List:
            binding.recyclerViewPost.layoutManager = LinearLayoutManager(requireActivity())

            postArrayList = arrayListOf<PostModel>()

            recylerRetrivie()

        binding.postText.setOnClickListener {
            val intent = Intent(requireActivity(), AddPostActivity::class.java)
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
                            val postData = ds.getValue(PostModel::class.java)
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