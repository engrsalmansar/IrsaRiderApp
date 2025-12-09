package com.irsakitchen.rider

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class IncomingOrderActivity : AppCompatActivity() {

    private val baseUrl = "https://irsakitchen.com"
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incoming_order)

        val orderId = intent.getStringExtra("order_id") ?: "0"
        val txt = findViewById<TextView>(R.id.txtOrderTitle)
        txt.text = "Order #$orderId"

        val accept = findViewById<Button>(R.id.btnAccept)
        val decline = findViewById<Button>(R.id.btnDecline)

        accept.setOnClickListener {
            stopRingtone()

            val prefs = getSharedPreferences("rider_prefs", Context.MODE_PRIVATE)
            val code = prefs.getInt("assign_id", 0)

            val json = JSONObject()
            json.put("id", orderId)
            json.put("rider_code", code)

            val body = RequestBody.create(
                "application/json".toMediaTypeOrNull(),
                json.toString()
            )

            val req = Request.Builder()
                .url("$baseUrl/accept.php")
                .post(body)
                .build()

            client.newCall(req).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    finish()
                }

                override fun onResponse(call: Call, response: Response) {
                    finish()
                }
            })
        }

        decline.setOnClickListener {
            stopRingtone()
            finish()
        }
    }

    private fun stopRingtone() {
        val service = Intent(this, RingtoneService::class.java)
        stopService(service)
    }
}
