package com.example.howtouseapis

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MyAdapter (val context : Activity, val productList : List<Product>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>(){
    class MyViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById<CircleImageView>(R.id.profile_image)
        val heading = itemView.findViewById<TextView>(R.id.headingText)
        val description = itemView.findViewById<TextView>(R.id.desText)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.each_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        val currentItem = productList[position]
//        holder.img.setImageURI(Uri.parse(productList[position].images[0]))
        Picasso.get().load(currentItem.images[0]).into(holder.img);
        holder.heading.text = currentItem.title
        holder.description.text = currentItem.description

    }

    override fun getItemCount(): Int {
        return productList.size
    }

}