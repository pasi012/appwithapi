package com.example.appwithapi.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appwithapi.R
import com.example.appwithapi.activities.DetailsActivity
import com.example.appwithapi.models.User


class UsersAdapter(private val userList: ArrayList<User>) : RecyclerView.Adapter<UsersAdapter.ViewHolder>(){
    private lateinit var context: Context

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val img = itemView.findViewById<ImageView>(R.id.imageView)
        val name = itemView.findViewById<TextView>(R.id.tvName)
        val email = itemView.findViewById<TextView>(R.id.tvEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.user_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]

        Glide.with(context).load(user.pic).into(holder.img)

        val stringBuilder = StringBuilder()
        stringBuilder.append(user.fName).append(" ").append(user.lName)

        holder.name.text = stringBuilder
        holder.email.text = user.emailId

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("img", user.pic)
            intent.putExtra("id", user.id)
            intent.putExtra("fname", user.fName)
            intent.putExtra("lname", user.lName)
            intent.putExtra("email", user.emailId)

            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }
}