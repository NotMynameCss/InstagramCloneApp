package edu.poly.instagramcloneapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
//import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import edu.poly.instagramcloneapp.R
import edu.poly.instagramcloneapp.model.postModel


//RecyclerView With Image: https://www.youtube.com/watch?v=0ok8e0JfIoo
class postAdapter(private val context: Context, private var recyclerViewpost:ArrayList<postModel>): RecyclerView.Adapter<postAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.user_name_search)
        val content: TextView = itemView.findViewById(R.id.description)
//        val layout_post:CardView = itemView.findViewById(R.id.layout_post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Create Viewholder every Line it need
        val view = LayoutInflater.from(parent.context).inflate(R.layout.posts_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //bind data with ViewHolder
        //We need an array of String

        val currentItem = recyclerViewpost[position]
        holder.name.text = currentItem.email
        holder.content.text = currentItem.content

    }

    override fun getItemCount(): Int {
        //Return size of items
        return recyclerViewpost.size
    }

}
