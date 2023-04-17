package com.politecnicomalaga.myktcrud.model.service

import android.util.Xml
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CallWebRetrofit {

//    @Headers("Accept: Application/xml")

    @GET("http://172.26.100.205:8585/Partes/resources/login")
    fun getResponse(@Query("usuario") usuario: String, @Query("password") password: String): Call<ResponseBody>

//    @GET("http://172.26.100.205:8585/Partes/resources/login?usuario={usuario}&password={password}")
//    fun getResponse(@Query("usuario") usuario: String, @Query("password") password: String): Call<ResponseBody>

//    @GET("http://172.26.100.205:8585/Partes/resources/login?usuario=i&password=i")
//    fun getResponse(): Call<ResponseBody>
}