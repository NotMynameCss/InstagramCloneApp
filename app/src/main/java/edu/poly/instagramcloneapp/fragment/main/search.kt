package edu.poly.instagramcloneapp.fragment.main

import android.app.ProgressDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage

import edu.poly.instagramcloneapp.databinding.FragmentSearchBinding
import java.io.File

//tìm ảnh và hiện: https://www.youtube.com/watch?v=A2-v2VFwLY0
//Upload ảnh: https://www.youtube.com/watch?v=GmpD2DqQYVk
class search : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(layoutInflater)


        return binding.root
    }

}