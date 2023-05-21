package edu.poly.instagramcloneapp.adapter
//Introduce: use for list Post In homefragment
//How make This can see Video Youtube in all PostModel
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
//import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import edu.poly.instagramcloneapp.R
import edu.poly.instagramcloneapp.activity.post.EditPostActivity
import edu.poly.instagramcloneapp.model.PostModel


class PostAdapter(private val context: Context, private var recyclerViewpost:ArrayList<PostModel>): RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.user_name_post)
        val content: TextView = itemView.findViewById(R.id.description)
        val imagebackground: ImageView = itemView.findViewById(R.id.post_background_Image)
        val layoutpost: CardView = itemView.findViewById(R.id.layout_post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Create Viewholder every Line it need
        val view = LayoutInflater.from(parent.context).inflate(R.layout.posts_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //We need an array of String
        val user = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val currentItem = recyclerViewpost[position]

        holder.username.text = currentItem.email
        holder.content.text = currentItem.content
        if (currentItem.image != null){
            holder.imagebackground.visibility = View.VISIBLE

            Glide.with(context)
                .load(currentItem.image)
                .placeholder(R.drawable.app_ic)
                .fitCenter()
                .into(holder.imagebackground)
        }else{
            holder.imagebackground.visibility = View.GONE
        }
        if (currentItem.uid == user){
            holder.layoutpost.setOnClickListener {
                val intent = Intent(context, EditPostActivity::class.java)

                intent.putExtra("uid",currentItem.uid)
                intent.putExtra("email",currentItem.email)
                intent.putExtra("content",currentItem.content)
                intent.putExtra("image",currentItem.image)
                intent.putExtra("timestamp",currentItem.timestamp)

                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        //Return size of items
        return recyclerViewpost.size
    }

}
