package edu.poly.instagramcloneapp.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.createBitmap
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.poly.instagramcloneapp.R
import edu.poly.instagramcloneapp.chatActivity

import edu.poly.instagramcloneapp.model.UserModel
import java.io.IOException

class multipleImageAdapter(private val context: Context, private var mutipleImageList:ArrayList<Uri>): RecyclerView.Adapter<userAdapter.ViewHolder>() {



    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //Use textview View

        val ImageView: ImageView = itemView.findViewById(R.id.imageView4)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userAdapter.ViewHolder {
        //Create Viewholder every Line it need
        val view = LayoutInflater.from(parent.context).inflate(R.layout.multi_image,parent,false)
        return userAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: userAdapter.ViewHolder, position: Int) {
        //bind data with ViewHolder
        //We need an array of String

        val currentItem = mutipleImageList[position]

        try {
            val bitmap:Bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver,currentItem)
            holder.ImageView.setImageBitmap(bitmap)
        }catch (e: IOException){
            e.printStackTrace()
        }




    }

    override fun getItemCount(): Int {
        //Return size of items
        if (mutipleImageList == null){
            return 0
        }else{
            return mutipleImageList.size
        }
    }


}

