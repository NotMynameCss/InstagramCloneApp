package edu.poly.instagramcloneapp.activity.person

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.poly.instagramcloneapp.R
import edu.poly.instagramcloneapp.activity.chatActivity
import edu.poly.instagramcloneapp.databinding.ActivityOtherProfileBinding

class otherProfile : AppCompatActivity() {
    private lateinit var binding: ActivityOtherProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_profile)
        binding = ActivityOtherProfileBinding.inflate(layoutInflater)


        val uid:String = intent.getStringExtra("uid").toString()
        val email:String = intent.getStringExtra("email").toString()
        val name:String = intent.getStringExtra("name").toString()

        binding.OtherSendMessage.setOnClickListener {
            val intent = Intent(this, chatActivity::class.java)
            intent.putExtra("uid",uid)
            intent.putExtra("name",name)
            intent.putExtra("name",email)
            this.startActivity(intent)
        }

        setContentView(binding.root)
    }
}