package com.example.mobileapp.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.mobileapp.R

class LeaderBoardRvAdapter (private val itemList: List<UserItem>) : RecyclerView.Adapter<LeaderBoardRvAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivUserPhoto: ImageView = itemView.findViewById(R.id.ivUserPhoto)
        private val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        private val tvPoints: TextView = itemView.findViewById(R.id.tvPoints)

        fun bind(user: UserItem) {
            val context = ivUserPhoto.context
            ivUserPhoto.load(user.photoUrl) {
                fallback(R.drawable.default_user_photo)
                transformations(CircleCropTransformation())
            }
            tvUserName.text = context.getString(R.string.first_second_names_pair, user.firstName, user.secondName)
            tvPoints.text = user.points.toString()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_rating, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = itemList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}