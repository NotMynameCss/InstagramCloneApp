package edu.poly.instagramcloneapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.fragment.app.Fragment
import edu.poly.instagramcloneapp.databinding.ActivitySignInBinding
import edu.poly.instagramcloneapp.fragment.login.SignUpPage
import edu.poly.instagramcloneapp.fragment.login.signInPage

//switch Fragment: https://www.youtube.com/watch?v=Q2HY58s9cHs

class login : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        replaceFragment(signInPage())



        binding = ActivitySignInBinding.inflate(layoutInflater);

        setContentView(binding.root)

        binding.signIn.setOnClickListener {
            replaceFragment(signInPage())

        }
        binding.signUp.setOnClickListener {
            replaceFragment(SignUpPage())
        }

    }


    private fun replaceFragment(fragment: Fragment){
        //Use supportFragmentManager to transaction
        val  fragmentManager = supportFragmentManager                        //Who
        val  fragmentTransaction = fragmentManager.beginTransaction()        //Who
        fragmentTransaction.replace(R.id.frame_login,fragment)                     //Who
        //finish
        fragmentTransaction.commit()
    }
}