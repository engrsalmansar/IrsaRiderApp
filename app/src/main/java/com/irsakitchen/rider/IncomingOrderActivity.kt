package com.irsakitchen.rider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class IncomingOrderActivity : AppCompatActivity() {

    private lateinit var txtOrderTitle: TextView
    private lateinit var btnAccept: Button
    private lateinit var btnReject: Button  // corrected name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incoming_order)

        txtOrderTitle = findViewById(R.id.txtOrderTitle)
        btnAccept = findViewById(R.id.btnAccept)
        btnReject = findViewById(R.id.btnReject)  // fixed ID

        txtOrderTitle.text = "You have a new order!"

        btnAccept.setOnClickListener {
            sendResponse(true)
        }

        btnReject.setOnClickListener {  // corrected
            sendResponse(false)
        }
    }

    private fun sendResponse(accepted: Boolean) {
        val url = "https://irsakitchen.com/api/rider_order_response.php"

        val json = JSONObject()
        json.put("accepted", accepted)

        val body = RequestBody.create(
            "application/json".toMediaTypeOrNull(),  // fixed import usage
            json.toString()
        )

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {}
        })
    }
}
