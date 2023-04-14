package edu.poly.instagramcloneapp.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import edu.poly.instagramcloneapp.R
import edu.poly.instagramcloneapp.databinding.ReceiverItemLayoutBinding
import edu.poly.instagramcloneapp.databinding.SentItemLayoutBinding
import edu.poly.instagramcloneapp.model.MessageModel
import edu.poly.instagramcloneapp.model.UserModel

//Read Message : https://www.youtube.com/watch?v=ch0TdgXICjQ&t=68s

//RecyclerView With Image: https://www.youtube.com/watch?v=0ok8e0JfIoo
class MessageAdapter(private val context: Context, private var list:ArrayList<MessageModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    //Pbiet Người dùng
    var ITEM_SENT = 1
    var ITEM_RECEIVER = 2


    //9:47
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == ITEM_SENT)
            SentViewHolder(LayoutInflater.from(context).inflate(R.layout.sent_item_layout,parent,false))
            else ReceiverViewHolder(
            LayoutInflater.from(context).inflate(R.layout.receiver_item_layout,parent,false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = list[position]


        if (holder.itemViewType == ITEM_SENT){
            val viewHolder = holder as SentViewHolder
            viewHolder.binding.SendTextView.text = message.message
        }else{
            val viewHolder = holder as ReceiverViewHolder
            viewHolder.binding.receiverTextView.text = message.message
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
    override fun getItemViewType(position: Int): Int {
        return if (FirebaseAuth.getInstance().uid == list[position].senderId) ITEM_SENT else ITEM_RECEIVER
    }
    inner class SentViewHolder(view: View): RecyclerView.ViewHolder(view){
        var binding = SentItemLayoutBinding.bind(view)
    }
    inner class ReceiverViewHolder(view: View): RecyclerView.ViewHolder(view){
        var binding = ReceiverItemLayoutBinding.bind(view)
    }
}