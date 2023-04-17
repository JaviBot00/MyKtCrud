package com.politecnicomalaga.myktcrud.view

import android.content.ContentValues
import android.content.Intent
import android.database.SQLException
import android.os.Bundle
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import com.politecnicomalaga.myktcrud.MainActivity
import com.politecnicomalaga.myktcrud.R
import com.politecnicomalaga.myktcrud.model.MySQLiteManager

class RegisterActivity : FormActivity() {

    private lateinit var btnSaveUser: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnSaveUser = findViewById(R.id.btnSaveUser)
        btnSaveUser.setOnClickListener {
            if (textInputUser.editText?.text.isNullOrEmpty()) {
                textInputUser.error = "Invalid User"
            } else if (textInputPassword.editText?.text.isNullOrEmpty()) {
                textInputPassword.error = "Incorrect Password"
            } else if (textInputBirthday.editText?.text.isNullOrEmpty()) {
                textInputBirthday.error = "Set a Date"
            } else if (textInputUserGroup.editText?.text.isNullOrEmpty()) {
                textInputUserGroup.error = "Choose a Rol"
            } else {
                val mySQLite = MySQLiteManager(this@RegisterActivity)
                mySQLite.setWritable()
                val myCursor = mySQLite.getOneUser(textInputUser.editText!!.text.toString())
                if (myCursor.moveToNext()) {
                    Snackbar.make(it, "This user already exist", Snackbar.LENGTH_LONG).show()
                } else {
                    try {
                        val values = ContentValues()
                        values.put(
                            MySQLiteManager.TUsers_USER[0], textInputUser.editText!!.text.toString()
                        )
                        values.put(
                            MySQLiteManager.TUsers_PASSWORD[0],
                            textInputPassword.editText!!.text.toString()
                        )
                        values.put(
                            MySQLiteManager.TUsers_BIRTHDATE[0],
                            textInputBirthday.editText!!.text.toString()
                        )
                        values.put(MySQLiteManager.TUsers_IMGPROFILE[0], myImageByteArray)
                        values.put(
                            MySQLiteManager.TUsers_ROLE[0],
                            textInputUserGroup.editText!!.text.toString()
                        )
                        mySQLite.insertUser(values)
                        mySQLite.getDb().close()
                        val result = Intent(this@RegisterActivity, MainActivity::class.java)
                        setResult(RESULT_OK, result)
                        finish()
                    } catch (e: SQLException) {
                        Snackbar.make(it, "Error at insert data", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}