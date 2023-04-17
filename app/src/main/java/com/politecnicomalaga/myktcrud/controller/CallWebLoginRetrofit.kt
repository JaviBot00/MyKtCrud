package com.politecnicomalaga.myktcrud.controller

import android.content.Context
import android.util.Xml
import android.widget.Toast
import com.google.gson.Gson
import com.politecnicomalaga.myktcrud.model.service.CallWebRetrofit
import com.politecnicomalaga.myktcrud.model.service.LoginResponse
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class CallWebLoginRetrofit() {
    private var retrofit: Retrofit? = null
    private val client: Retrofit
        get() {
            if (retrofit == null) {
                retrofit =
                    Retrofit.Builder().baseUrl("http://172.26.100.205:8585/Partes/resources/")
                        .client(OkHttpClient())
                        .addConverterFactory(SimpleXmlConverterFactory.create()).build()
            }
            return retrofit!!
        }

    fun loadData(myContext: Context) {
        val apiService = client.create(CallWebRetrofit::class.java)

        apiService.getResponse("i", "i").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val posts = response.body()!!.string()
                val gson = Gson()
                val loginResponse = gson.fromJson(posts, LoginResponse::class.java)
                Toast.makeText(
                    myContext,
                    loginResponse.getOperario()?.getPersona()?.getDenominacionSocial(),
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    myContext, t.toString(), Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}