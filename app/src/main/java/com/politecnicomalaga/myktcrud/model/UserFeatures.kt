package com.politecnicomalaga.myktcrud.model

import android.graphics.Bitmap

class UserFeatures {

    lateinit var username: String
    lateinit var password: String
    lateinit var birthday: String
    var imgProfile: Bitmap? = null
    lateinit var userRol: String
}