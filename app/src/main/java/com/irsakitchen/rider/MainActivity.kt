package com.irsakitchen.rider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var inputAssignId: EditText
    private lateinit var btnLogin: Button
    private lateinit var txtStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputAssignId = findViewById(R.id.inputAssignId)
        btnLogin = findViewById(R.id.btnLogin)
        txtStatus = findViewById(R.id.txtStatus)

        btnLogin.setOnClickListener {
            val id = inputAssignId.text.toString().trim()
            if (id.isEmpty()) {
                txtStatus.text = "Please enter a Rider ID"
                return@setOnClickListener
            }

            txtStatus.text = "Registering Rider..."
            registerRider(id)
        }
    }

    private fun registerRider(riderId: String) {
        val url = "https://irsakitchen.com/api/register_rider.php"

        val json = JSONObject()
        json.put("assign_id", riderId)

        val body = json
            .toString()
            .toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    txtStatus.text = "Network Error: ${e.message}"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    txtStatus.text = "Rider Registered Successfully!"
                }
            }
        })
    }
}
