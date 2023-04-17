package com.politecnicomalaga.myktcrud.controller

import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import com.google.gson.Gson
import com.politecnicomalaga.myktcrud.model.service.LoginResponse
import okhttp3.OkHttpClient
import okhttp3.Request

class CallWebLoginService(val myContext: Context) : AsyncTask<Void, Void, String>() {
    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Void?): String {
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("http://172.26.100.205:8585/Partes/resources/login?usuario=i&password=i")
                .build()
            val response = client.newCall(request).execute()
            return response.body?.string() ?: ""
        } catch (e: Exception) {
            Toast.makeText(
                myContext, e.toString(), Toast.LENGTH_LONG
            ).show()
        }
        return ""
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: String?) {
//            super.onPostExecute(result)
        val gson = Gson()
        val loginResponse = gson.fromJson(result, LoginResponse::class.java)
        if (loginResponse.getRespuesta()?.id.toString() == "1") {
            Toast.makeText(
                myContext,
                loginResponse.getOperario()?.getPersona()?.getDenominacionSocial(),
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                myContext, loginResponse.getRespuesta()?.mensaje.toString(), Toast.LENGTH_LONG
            ).show()
        }
    }
}