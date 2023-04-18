package edu.poly.instagramcloneapp.fragment.main


//Nguồn tham khảo:
    // Connect firebase:  https://www.youtube.com/watch?v=YOT8P1PtJQg&t=618s
    //Viewbinding in fragment: https://www.youtube.com/watch?v=v11x54y5YVc
    //SignIn and SignOut : https://www.youtube.com/watch?v=idbxxkF1l6k&list=PLQFUWT9wUMWEXj9AVanMZg1tRUGr95aBr&index=10&t=784s
    //Upload ảnh: https://www.youtube.com/watch?v=GmpD2DqQYVk
import android.app.Activity
import android.app.ProgressDialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

import edu.poly.instagramcloneapp.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.*


class home : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    //Firbase Image
        //Image
        private lateinit var imageUri:Uri

    //Firebase Database:
        private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Chung của Home
            binding = FragmentHomeBinding.inflate(layoutInflater)
            dbRef = FirebaseDatabase.getInstance().getReference("User")

        //Cần cho onCreateView()
            return binding.root
    }


}