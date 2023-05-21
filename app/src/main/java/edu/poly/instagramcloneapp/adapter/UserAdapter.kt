package edu.poly.instagramcloneapp.adapter
//Introduce: use for list_User in fragment and Friend_List
//How make This can see Video Youtube in all UsertModel
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.poly.instagramcloneapp.R
import edu.poly.instagramcloneapp.activity.person.OtherProfile
import edu.poly.instagramcloneapp.model.UserModel

class UserAdapter(private val context: Context, private var recyclerViewSearch:ArrayList<UserModel>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        //Use textview View
        val nameView: TextView = itemView.findViewById(R.id.nameViewUser)
        val emailView: TextView = itemView.findViewById(R.id.emailViewUser)
        val imageView: ImageView = itemView.findViewById(R.id.imageReceiverView)
        val layoutUser: RelativeLayout = itemView.findViewById(R.id.layoutUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Create Viewholder every Line it need
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_user,parent,false)
        return ViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = recyclerViewSearch[position]
        holder.nameView.text = currentItem.name
        holder.emailView.text = currentItem.email

            Glide.with(context)
                .load(currentItem.imageUrl)
                .placeholder(R.drawable.app_ic)
                .fitCenter()
                .into(holder.imageView)

        holder.layoutUser.setOnClickListener {
            val intent = Intent(context, OtherProfile::class.java)

            intent.putExtra("uid",currentItem.uid)
            intent.putExtra("name",currentItem.name)
            intent.putExtra("email",currentItem.email)
            intent.putExtra("bio",currentItem.bio)
            intent.putExtra("image",currentItem.imageUrl)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        //Return size of items
        return recyclerViewSearch.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun searchDataList(searchList: ArrayList<UserModel>){
        recyclerViewSearch = searchList
        notifyDataSetChanged()
    }

}



