package com.politecnicomalaga.myktcrud

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.politecnicomalaga.myktcrud.model.service.LoginResponse
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.politecnicomalaga.myktcrud.model.SQLiteManager
import com.politecnicomalaga.myktcrud.view.RecyclerviewActivity
import com.politecnicomalaga.myktcrud.view.RegisterActivity
import okhttp3.OkHttpClient
import okhttp3.Request


class MainActivity : AppCompatActivity() {

    private val REGISTER_REQUEST = 1
    private val RECYCLER_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        val textInputUser: TextInputLayout = findViewById(R.id.txtFldUser)
        val textInputPassword: TextInputLayout = findViewById(R.id.txtFldPassWord)
        val btnAccess: Button = findViewById(R.id.btnAccess)
        val btnRegister: Button = findViewById(R.id.btnRegister)

        textInputUser.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isNotEmpty()) {
                    textInputUser.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        textInputPassword.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isNotEmpty()) {
                    textInputPassword.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnAccess.setOnClickListener {
            if (textInputUser.editText?.text.isNullOrEmpty()) {
                textInputUser.error = "Invalid User"
            } else if (textInputPassword.editText?.text.isNullOrEmpty()) {
                textInputPassword.error = "Incorrect Password"
            } else {
                val mySQLite = SQLiteManager(this@MainActivity)
                mySQLite.setWritable()
                val myCursor = mySQLite.getUserLogin(
                    textInputUser.editText!!.text.toString(),
                    textInputPassword.editText!!.text.toString()
                )
                if (myCursor.moveToNext()) {
                    mySQLite.getDb().close()
                    textInputUser.editText!!.text.clear()
                    textInputPassword.editText!!.text.clear()
                    startActivityForResult(
                        Intent(
                            this@MainActivity, RecyclerviewActivity::class.java
                        ), RECYCLER_REQUEST
                    )
                } else {
                    Snackbar.make(it, "This user not exist", Snackbar.LENGTH_LONG).show()
                }
            }
        }

        btnRegister.setOnClickListener {
            startActivityForResult(
                Intent(this@MainActivity, RegisterActivity::class.java), REGISTER_REQUEST
            )
//            DownloadFilesTask().execute()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REGISTER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "User successfully registered",
                    Snackbar.LENGTH_LONG
                ).show()
            } else if (resultCode == RESULT_CANCELED) {
                Snackbar.make(
                    findViewById(android.R.id.content), "User not registered", Snackbar.LENGTH_LONG
                ).show()
            }
        }

        if (requestCode == RECYCLER_REQUEST) {
//            if (resultCode == RESULT_OK) {
            Snackbar.make(
                findViewById(android.R.id.content), "Successful log out", Snackbar.LENGTH_LONG
            ).show()
        }
//        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class DownloadFilesTask : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {
//            val url = URL("http://172.26.100.205:8585/Partes/resources/login?usuario=i&password=i")
////            val url =
////                URL("http://172.26.100.205:8585/Partes/resources/login?usuario=" + txt + "i&password=i")
//            val conn = url.openConnection() as HttpURLConnection
//            conn.setRequestProperty("Content-Type", "application/json;")
////            val status = conn.responseCode
////            val gson = Gson()
////            var loginResponse = LoginResponse()
////            if (status == HttpURLConnection.HTTP_OK) {
////                loginResponse =
////                    gson.toJson(conn.responseMessage, LoginResponse::class.java) as LoginResponse
////            }
//            return conn.responseMessage.toString()
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://172.26.100.205:8585/Partes/resources/login?usuario=i&password=i")
                    .build()
                val response = client.newCall(request).execute()
                return response.body?.string() ?: ""
            } catch (e: Exception) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    e.toString(),
                    Snackbar.LENGTH_LONG
                ).show()
            }
            return ""
        }

        override fun onPostExecute(result: String?) {
//            super.onPostExecute(result)
            val gson = Gson()
            val loginResponse = gson.fromJson(result, LoginResponse::class.java)
            if (loginResponse.getRespuesta()?.id.toString() == "1") {
                Toast.makeText(
                    this@MainActivity,
                    loginResponse.getOperario()?.getPersona()?.getDenominacionSocial(),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    loginResponse.getRespuesta()?.mensaje.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}