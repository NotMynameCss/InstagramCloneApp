package edu.poly.instagramcloneapp.adapter
//Introduce: use for List MultipleImagePicker in chatActivity
//How make this can see In Message_Model


import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import androidx.recyclerview.widget.RecyclerView
import edu.poly.instagramcloneapp.R

import java.io.IOException

@Suppress("DEPRECATION")
class MultipleImageAdapter(private val context: Context, private var mutipleImageList:ArrayList<Uri>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //Use textview View

        val imageView: ImageView = itemView.findViewById(R.id.imageView4)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        //Create Viewholder every Line it need
        val view = LayoutInflater.from(parent.context).inflate(R.layout.multi_image,parent,false)
        return UserAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        //bind data with ViewHolder
        //We need an array of String

        val currentItem = mutipleImageList[position]

        try {
            val bitmap:Bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver,currentItem)
            holder.imageView.setImageBitmap(bitmap)
        }catch (e: IOException){
            e.printStackTrace()
        }




    }

    override fun getItemCount(): Int {
        //Return size of items
        return mutipleImageList.size
    }


}

