package com.politecnicomalaga.myktcrud.model

import android.graphics.Bitmap
import java.io.Serializable

class UserFeatures(
    private var userName: String,
    private var passWord: String,
    private var birthDay: String,
    private var imgProfile: Bitmap?,
    private var userRol: String
) : Serializable {


    fun getUserName(): String {
        return userName
    }

    fun setUserName(Username: String) {
        this.userName = Username
    }

    fun getPassWord(): String {
        return passWord
    }

    fun setPassWord(Password: String) {
        this.passWord = Password
    }

    fun getBirthday(): String {
        return birthDay
    }

    fun setBirthday(Birthday: String) {
        this.birthDay = Birthday
    }

    fun getImgProfile(): Bitmap? {
        return imgProfile
    }

    fun setImgProfile(imgProfile: Bitmap) {
        this.imgProfile = imgProfile
    }

    fun getUserRol(): String {
        return userRol
    }

    fun setUserRol(userRol: String) {
        this.userRol = userRol
    }
}