package edu.poly.instagramcloneapp.adapter
//Introduce: Use for message_list in RoomChat
//Yu can watch Video How do this in messageModel
import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

import edu.poly.instagramcloneapp.R
import edu.poly.instagramcloneapp.databinding.ReceiverItemLayoutBinding
import edu.poly.instagramcloneapp.databinding.SentItemLayoutBinding
import edu.poly.instagramcloneapp.model.MessageModel

@Suppress("DEPRECATION")
class MessageAdapter(private val context: Context, private var messagelist:ArrayList<MessageModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var senderRoom: String

    //Phân biệt Người dùng
    private var itemsent = 1
    private var itemreceived = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == itemsent)
            SentViewHolder(LayoutInflater.from(context).inflate(R.layout.sent_item_layout,parent,false))
            else ReceiverViewHolder(
            LayoutInflater.from(context).inflate(R.layout.receiver_item_layout,parent,false)
        )
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messagelist[position]
        val handler = Handler() //SetTimer

        senderRoom = message.senderId + message.receiverId
        if (holder.itemViewType == itemsent){
            val viewHolder = holder as SentViewHolder

            if (message.message.isNullOrBlank()){
                //Need:
                    viewHolder.binding.linearImageView.visibility = View.VISIBLE
                    viewHolder.binding.linearTextView.visibility = View.GONE
                //Action:
                    Glide.with(context)
                        .load(message.Image)
                        .placeholder(R.drawable.app_ic)
                        .fitCenter()
                        .into(viewHolder.binding.sendImageView)
            }else{
                viewHolder.binding.SendTextView.text = message.message
                if (viewHolder.binding.timeStamp.visibility == View.GONE){
                    viewHolder.binding.SendTextView.setOnClickListener {

                        viewHolder.binding.timeStamp.visibility = View.VISIBLE
                        viewHolder.binding.timeStamp.text = message.timestamp
                        handler.postDelayed({
                            viewHolder.binding.timeStamp.visibility = View.GONE
                        },3000)// 1000 = 1s, sau 3s sẽ tự động mất
                    }
                }
            }
        }//Not use Image With Receiver yet
        else{
            val viewHolder = holder as ReceiverViewHolder
            viewHolder.binding.receiverTextView.text = message.message
        }
    }

    override fun getItemCount(): Int {
        return messagelist.size
    }
    override fun getItemViewType(position: Int): Int {
        return if (FirebaseAuth.getInstance().uid == messagelist[position].senderId) itemsent else itemreceived
    }
    inner class SentViewHolder(view: View): RecyclerView.ViewHolder(view){
        var binding = SentItemLayoutBinding.bind(view)
    }
    inner class ReceiverViewHolder(view: View): RecyclerView.ViewHolder(view){
        var binding = ReceiverItemLayoutBinding.bind(view)
    }
}