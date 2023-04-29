package edu.poly.instagramcloneapp.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.poly.instagramcloneapp.R
import edu.poly.instagramcloneapp.activity.chatActivity
import edu.poly.instagramcloneapp.activity.person.otherProfile
import edu.poly.instagramcloneapp.model.UserModel


//RecyclerView With Image: https://www.youtube.com/watch?v=0ok8e0JfIoo
class userAdapter(private val context: Context,private var recyclerViewSearch:ArrayList<UserModel>): RecyclerView.Adapter<userAdapter.ViewHolder>() {



    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        //Use textview View
        val nameView: TextView = itemView.findViewById(R.id.receiverTextView)
        val emailView: TextView = itemView.findViewById(R.id.textView2)
        val ImageView: ImageView = itemView.findViewById(R.id.imageReceiverView)
        val layoutUser: RelativeLayout = itemView.findViewById(R.id.layoutUser)
    }


    //Step 4: 4:08s - 5:04s
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Create Viewholder every Line it need
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item,parent,false)
        return ViewHolder(view)
    }


    //Step 5: 5:45s - 6:00s
    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //bind data with ViewHolder
        //We need an array of String

        val currentItem = recyclerViewSearch[position]
        holder.nameView.text = currentItem.name
        holder.emailView.text = currentItem.email


            Glide.with(context)
                .load(currentItem.imageUrl)
                .placeholder(R.drawable.app_ic)
                .fitCenter()
                .into(holder.ImageView)

        //For open chat Acitivity
        holder.layoutUser.setOnClickListener {
            val intent = Intent(context, otherProfile::class.java)
            intent.putExtra("uid",currentItem.uid)
            intent.putExtra("name",currentItem.name)
            intent.putExtra("email",currentItem.email)
            intent.putExtra("bio",currentItem.bio)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        //Return size of items
        return recyclerViewSearch.size
    }
    fun searchDataList(searchList: ArrayList<UserModel>){
        recyclerViewSearch = searchList
        notifyDataSetChanged()
    }

}



