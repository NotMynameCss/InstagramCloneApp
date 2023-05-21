package edu.poly.instagramcloneapp.activity
//Introduce this activity: use for switching fragments:Home,Person,friend,Favorite,Search



import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import edu.poly.instagramcloneapp.R

import edu.poly.instagramcloneapp.databinding.ActivityMainBinding
import edu.poly.instagramcloneapp.fragment.main.*

class MainActivity : AppCompatActivity() {
    //Chung:
        private lateinit var binding: ActivityMainBinding
    //Firebase:
        private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        //Chung:
            super.onCreate(savedInstanceState)
            firebaseAuth = FirebaseAuth.getInstance()
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
        //Action:
            replaceFragment(Home())
            binding.navView.setOnItemSelectedListener {
                when(it.itemId){
                    R.id.nav_home -> replaceFragment(Home())
                    R.id.nav_profile -> replaceFragment(Person())
                    R.id.nav_add_post -> replaceFragment(FriendList())
                    R.id.nav_favorite -> replaceFragment(Favorite())
                    R.id.nav_search -> replaceFragment(Search())
                }
                true
            }
    }
    private fun replaceFragment(fragment: Fragment){
        //Need:
            val  fragmentManager = supportFragmentManager                        //Who
            val  fragmentTransaction = fragmentManager.beginTransaction()        //Who
        //action:
            fragmentTransaction.replace(R.id.frame_id,fragment)
            fragmentTransaction.addToBackStack(null)

            fragmentTransaction.commit()
    }
}
