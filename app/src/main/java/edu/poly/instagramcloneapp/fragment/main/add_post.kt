package edu.poly.instagramcloneapp.fragment.main

import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import edu.poly.instagramcloneapp.Adapter.userAdapter
import edu.poly.instagramcloneapp.databinding.FragmentAddPostBinding
import edu.poly.instagramcloneapp.model.UserModel


class add_post : Fragment() {

    private lateinit var binding: FragmentAddPostBinding

    //Retrivie:
    private var firebaseDatabase: FirebaseDatabase?=null
    private var databaseReference: DatabaseReference?= null
    private lateinit var userArrayList: ArrayList<UserModel>
    private lateinit var adapter2: userAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddPostBinding.inflate(layoutInflater)


        //Chung của Retrivie-1
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("data")!! //For snapshot, Names Is parent

        firebaseAuth = FirebaseAuth.getInstance()
        //Bắt đầu Gọi lên Recyler:
        binding.recyclerViewSearch.layoutManager = LinearLayoutManager(requireActivity())

        userArrayList = arrayListOf<UserModel>()

        recylerRetrivie()

        //Button:
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(p0: String): Boolean {
                searchList(p0)
                return true
            }
        })
        return binding.root
    }
    private fun searchList(text:String){
        //Cần cho Phần search
        adapter2 = userAdapter(requireActivity(),userArrayList)
        binding.recyclerViewSearch.adapter = adapter2

        //Bắt đầu search
        val searchList = ArrayList<UserModel>()
        for (userdata in userArrayList){
            if (userdata.name?.lowercase()?.contains(text.lowercase()) == true){
                searchList.add(userdata)
            }
        }
        adapter2.searchDataList(searchList)
    }

    private fun recylerRetrivie() {
        databaseReference = FirebaseDatabase.getInstance().getReference("friend")

        databaseReference?.child(firebaseAuth.currentUser?.uid.toString())?.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()){
                        userArrayList.clear()
                        for(ds in snapshot.children){
                            val userData = ds.getValue(UserModel::class.java)

                                userArrayList.add(userData!!)
                        }
                        binding.recyclerViewSearch.adapter = userAdapter(requireActivity(),userArrayList)
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            }
        )
    }

}