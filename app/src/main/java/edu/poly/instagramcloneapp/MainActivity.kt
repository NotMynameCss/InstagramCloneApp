package edu.poly.instagramcloneapp

//Change start Acitivity: https://www.youtube.com/watch?v=6eES56mxfMs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import edu.poly.instagramcloneapp.databinding.ActivityMainBinding
import edu.poly.instagramcloneapp.fragment.main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        //Bottom Navigation
        replaceFragment(home())


        //viewBinding
        binding = ActivityMainBinding.inflate(layoutInflater);


        //binding call function Navigation
        binding.navView.setOnItemSelectedListener { //Kick hoat khi an an vao menu item
            when(it.itemId){
                R.id.nav_home -> replaceFragment(home())
                R.id.nav_profile -> replaceFragment(person())
                R.id.nav_add_post -> replaceFragment(add_post())
                R.id.nav_favorite-> replaceFragment(favorite())
                R.id.nav_search-> replaceFragment(search())

                else->{

                }
            }
            true
        }
    setContentView(binding.root)
    }


    //    create fragment for bottom_nav
    private fun replaceFragment(fragment: Fragment){
        //Use supportFragmentManager to transaction
        val  fragmentManager = supportFragmentManager                        //Who
        val  fragmentTransaction = fragmentManager.beginTransaction()        //Who
        fragmentTransaction.replace(R.id.frame_id,fragment)                     //Who
        //finish
        fragmentTransaction.commit()
    }
}
