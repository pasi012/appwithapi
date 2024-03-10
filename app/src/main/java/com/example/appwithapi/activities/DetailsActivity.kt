package com.example.appwithapi.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.appwithapi.R


class DetailsActivity : AppCompatActivity() {

    private lateinit var imgAvatar: ImageView
    private lateinit var idAvatar: TextView
    private lateinit var fnameAvatar: TextView
    private lateinit var lnameAvatar: TextView
    private lateinit var emailAvatar: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        imgAvatar =  findViewById(R.id.imgAvatar)
        idAvatar = findViewById(R.id.idAvatar)
        fnameAvatar = findViewById(R.id.fnameAvatar)
        lnameAvatar = findViewById(R.id.lnameAvatar)
        emailAvatar = findViewById(R.id.emailAvatar)

        val intent = intent

        val img = intent.getStringExtra("img")
        val id = intent.getStringExtra("id")
        val fname = intent.getStringExtra("fname")
        val lname = intent.getStringExtra("lname")
        val email = intent.getStringExtra("email")

        Glide.with(this).load(img).into(imgAvatar)
        idAvatar.text = id
        fnameAvatar.text = fname
        lnameAvatar.text = lname
        emailAvatar.text = email

    }
}