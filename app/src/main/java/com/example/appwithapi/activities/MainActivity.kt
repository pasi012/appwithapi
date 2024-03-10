package com.example.appwithapi.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.appwithapi.R
import com.example.appwithapi.adapters.UsersAdapter
import com.example.appwithapi.models.User

class MainActivity : AppCompatActivity() {

    var userList = arrayListOf<User>()
    val apiSample = "https://reqres.in/api/users"
    var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView =  findViewById(R.id.recyclerView)

        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(Request.Method.GET,apiSample, null, { res ->
            Log.d("Volley Sample", res.getString("page"))

            val jsonArray = res.getJSONArray("data")
            for (i in 0 until jsonArray.length()){
                val jsonObj = jsonArray.getJSONObject(i)

                val user = User(
                    jsonObj.getString("id"),
                    jsonObj.getString("email"),
                    jsonObj.getString("first_name"),
                    jsonObj.getString("last_name"),
                    jsonObj.getString("avatar")
                )

                userList.add(user)
            }

            recyclerView?.layoutManager = LinearLayoutManager(this)
            recyclerView?.adapter = UsersAdapter(userList)



        }, {err ->
            Log.d("Volley Sample Fail", err.message.toString())
        })

        reqQueue.add(request)

    }
}