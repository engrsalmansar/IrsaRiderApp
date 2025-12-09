package com.irsakitchen.rider

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private val baseUrl = "https://irsakitchen.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val assignInput = findViewById<EditText>(R.id.inputAssignId)
        val status = findViewById<TextView>(R.id.txtStatus)
        val loginBtn = findViewById<Button>(R.id.btnLogin)

        loginBtn.setOnClickListener {
            val id = assignInput.text.toString().trim()
            if (id.isEmpty()) {
                status.text = "Enter Rider Assign ID"
                return@setOnClickListener
            }
            loginRider(id.toInt(), status)
        }
    }

    private fun loginRider(assignId: Int, status: TextView) {
        status.text = "Logging in..."

        val json = JSONObject()
        json.put("assign_id", assignId)

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            json.toString()
        )

        val req = Request.Builder()
            .url("$baseUrl/api/rider_login.php")
            .post(body)
            .build()

        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { status.text = "Network error: ${e.message}" }
            }

            override fun onResponse(call: Call, resp: Response) {
                val bodyStr = resp.body?.string() ?: ""
                if (!resp.isSuccessful) {
                    runOnUiThread { status.text = "HTTP ${resp.code}" }
                    return
                }

                try {
                    val json = JSONObject(bodyStr)
                    if (!json.getBoolean("ok")) {
                        runOnUiThread { status.text = json.getString("error") }
                        return
                    }

                    val rider = json.getJSONObject("rider")
                    val riderId = rider.getInt("id")

                    val prefs = getSharedPreferences("rider_prefs", Context.MODE_PRIVATE)
                    prefs.edit()
                        .putInt("assign_id", assignId)
                        .putInt("rider_id", riderId)
                        .apply()

                    runOnUiThread { status.text = "Welcome!" }

                    FirebaseMessaging.getInstance().token
                        .addOnSuccessListener { token ->
                            registerDevice(riderId, token, status)
                        }

                } catch (e: Exception) {
                    runOnUiThread { status.text = "Bad response" }
                }
            }
        })
    }

    private fun registerDevice(riderId: Int, token: String, status: TextView) {
        val json = JSONObject()
        json.put("rider_id", riderId)
        json.put("fcm_token", token)
        json.put("platform", "android")

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            json.toString()
        )

        val req = Request.Builder()
            .url("$baseUrl/api/register_device.php")
            .post(body)
            .build()

        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { status.text = "Registered but token failed" }
            }

            override fun onResponse(call: Call, resp: Response) {
                runOnUiThread { status.text = "Ready" }
            }
        })
    }
}
