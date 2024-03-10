package com.example.appwithapi.activities

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.appwithapi.ApiClient
import com.example.appwithapi.R
import com.example.appwithapi.models.LoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import com.google.gson.annotations.SerializedName
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private var countdownTimer: CountDownTimer? = null

    private var loginAttempts = 1
    private var isLoginBlocked = false
    private var blockEndTime: Long = 0

    lateinit var displayBlockTimeMunite: TextView
    lateinit var displayBlockTimeSecond: TextView

    lateinit var loginBtn: Button

    lateinit var blockTime: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        displayBlockTimeMunite = findViewById(R.id.blockTimeMunite)
        displayBlockTimeSecond = findViewById(R.id.blockTimeSecond)

        blockTime = findViewById(R.id.linearTimeBlock)

        blockTime.visibility = View.INVISIBLE

        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        loginBtn = findViewById(R.id.buttonLogin)

        loginBtn.setOnClickListener {

            if (!isLoginBlocked) {

                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                // Dummy login validation
                if (email == "eve.holt@reqres.in" && password == "cityslicka") {

                    val request = LoginRequest(email, password)

                    ApiClient.apiService.login(request).enqueue(object : Callback<LoginResponse> {
                        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                            if (response.isSuccessful) {
                                val token = response.body()?.token
                                Toast.makeText(applicationContext, "Login Successful. Token: $token", Toast.LENGTH_SHORT).show()
                                // Handle successful login, e.g., navigate to another activity

                                loadMain()

                            } else {
                                Toast.makeText(applicationContext, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
                                // Handle login failure
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
                            // Handle failure to connect to the server or other network issues
                        }
                    })

                } else {
                    // Invalid credentials
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()

                    loginAttempts++
                    if (loginAttempts >= 3) {
                        // Block login screen for 2 minutes
                        blockLoginScreen()
                    }

                }

            } else {

                val remainingTime = blockEndTime - System.currentTimeMillis()

                if (remainingTime > 0) {
                    val minutes = remainingTime / (1000 * 60)
                    val seconds = (remainingTime / 1000) % 60
                    val message = "Login blocked. Try again in $minutes minutes and $seconds seconds"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    startCountdownTimer()
                } else {
                    // Unblock login screen
                    unblockLoginScreen()
                }

            }

        }

    }

    fun loadMain(){
        // Proceed to the main activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun blockLoginScreen() {
        isLoginBlocked = true
        blockEndTime = System.currentTimeMillis() + 2 * 60 * 1000 // 2 minutes

    }

    private fun unblockLoginScreen() {
        isLoginBlocked = false
        loginAttempts = 1
        cancelCountdownTimer()
    }

    private fun startCountdownTimer() {
        countdownTimer = object : CountDownTimer(blockEndTime - System.currentTimeMillis(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / (1000 * 60)
                val seconds = (millisUntilFinished / 1000) % 60
                val timeLeft = "Try again in $minutes minutes and $seconds seconds"

//                Toast.makeText(this@LoginActivity, timeLeft, Toast.LENGTH_SHORT).show()

                blockTime.visibility = View.VISIBLE
                displayBlockTimeMunite.text = minutes.toString()
                displayBlockTimeSecond.text = seconds.toString()

                loginBtn.visibility = View.INVISIBLE


            }

            override fun onFinish() {
                unblockLoginScreen()
            }
        }.start()
    }

    private fun cancelCountdownTimer() {

        countdownTimer?.cancel()

        blockTime.visibility = View.INVISIBLE
        displayBlockTimeMunite.text = ""
        displayBlockTimeSecond.text = ""

        loginBtn.visibility = View.VISIBLE

    }

}

interface ApiService {
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}

data class LoginResponse(
    @SerializedName("token")
    val token: String
)