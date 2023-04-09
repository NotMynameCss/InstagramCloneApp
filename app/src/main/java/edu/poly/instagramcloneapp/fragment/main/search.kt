package edu.poly.instagramcloneapp.fragment.main


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import edu.poly.instagramcloneapp.Adapter.userAdapter
import edu.poly.instagramcloneapp.databinding.FragmentSearchBinding
import edu.poly.instagramcloneapp.model.UserModel

//Simple Recyler: https://www.youtube.com/watch?v=Y4S4KNJzmGI


//Update: https://www.youtube.com/watch?v=srQ0Nq3mJ_M&t=139s

// Retrivie + snapshot Data: https://www.youtube.com/watch?v=7YPbd6gRPyk&list=PLKqtGJUS11t-1s4bd_u6wyuBEEr2fRhMX

//Retrivie + recylerview: https://www.youtube.com/watch?v=VVXKVFyYQdQ



//* Retrivie : Bấm để nhận kết quả(search), Fetch Tự hiện kết quả(Profile)
//* Snapshot có thể kiểm tra và chỉ gọi những gì tồn tại dù trong biến có thứ ko tồn tại


class search : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    //Retrivie:
    private var firebaseDatabase: FirebaseDatabase?=null
    private var databaseReference: DatabaseReference?= null
    private lateinit var userArrayList: ArrayList<UserModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(layoutInflater)

        //Chung của Retrivie-1
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("data")!! //For snapshot, Names Is parent

        //Bắt đầu Gọi lên Recyler:
        binding.recyclerViewSearch.layoutManager = LinearLayoutManager(requireActivity())

        userArrayList = arrayListOf<UserModel>()

        recylerRetrivie()

        return binding.root
    }

    private fun recylerRetrivie() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users")

        databaseReference?.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()){

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