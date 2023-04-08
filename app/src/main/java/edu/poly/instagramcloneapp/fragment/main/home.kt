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
        //firebase Object
            dbRef = FirebaseDatabase.getInstance().getReference("User")
        //Image Firebase
            binding.Pick.setOnClickListener {
                pickImage()
            }
            binding.Upload.setOnClickListener {
                UploadImage()
        }




        //Cần cho onCreateView()
            return binding.root
    }
    //Data for firebase
//    private fun saveUserData(){
//        val email_User = binding.EmailInput.text.toString()
//
//        val user_id = dbRef.push().key!!
//
//        // Push to model in UserModel
//        val users = UserModel(user_id,email_User)
//
//        dbRef.child(user_id).setValue(users)
//
//    }
    //Image Firebase
       private  fun UploadImage(){

            val progressDialog = ProgressDialog(requireActivity())
            progressDialog.setMessage("UploadImage ...")

            progressDialog.setCancelable(false)
            progressDialog.show()


            //Date Upload
            val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
            val now = Date()
            // Tạo thư mục lưu Image
                val fileName = formatter.format(now)
                val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

            //Commit Upload
                storageReference.putFile(imageUri)
                .addOnSuccessListener {
                    binding.imageView.setImageURI(null)
                    Toast.makeText(requireActivity(),"SuccessFull Uploaded",Toast.LENGTH_SHORT).show()
                    if (progressDialog.isShowing){
                        progressDialog.dismiss()
                    }
                }
                .addOnFailureListener {
                    if (progressDialog.isShowing){
                        progressDialog.dismiss()
                        Toast.makeText(requireActivity(),"Failed",Toast.LENGTH_SHORT).show()

                    }
                }
        }

    private fun pickImage(){

        val intent = Intent()
        intent.type = "image/*" //* cho phép tất cả loại ảnh: JPEG,PNG,.....

        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent,100)

    }
    //Support for Image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == 100 && resultCode == Activity.RESULT_OK){ //Acitivity is need if this is fragment

            imageUri = data?.data!!
            binding.imageView.setImageURI(imageUri)
        }
    }

}