package edu.poly.instagramcloneapp.fragment.main

import android.content.Intent
import android.os.Bundle
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import edu.poly.instagramcloneapp.R
import edu.poly.instagramcloneapp.databinding.FragmentAddPostBinding
import edu.poly.instagramcloneapp.model.MessageModel
import java.util.Date


class add_post : Fragment() {

    private lateinit var binding: FragmentAddPostBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddPostBinding.inflate(layoutInflater)



        return binding.root
    }


}