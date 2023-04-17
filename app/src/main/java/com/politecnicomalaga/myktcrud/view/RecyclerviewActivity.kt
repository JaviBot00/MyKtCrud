package com.politecnicomalaga.myktcrud.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.politecnicomalaga.myktcrud.R
import com.politecnicomalaga.myktcrud.controller.UsersRVAdapter
import com.politecnicomalaga.myktcrud.model.SQLiteManager
import com.politecnicomalaga.myktcrud.model.UserFeatures

class RecyclerviewActivity : AppCompatActivity() {

    private val myUsers = ArrayList<UserFeatures>()
    lateinit var myAdapter: UsersRVAdapter
    private lateinit var myRecyclerView: RecyclerView
    val EDIT_REQUEST = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        myRecyclerView = this.findViewById(R.id.rvReport)

        myAdapter = UsersRVAdapter(this, myUsers)
        cargarVista()
        myRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        myRecyclerView.adapter = myAdapter
        myAdapter.notifyDataSetChanged()

        myAdapter.setInterEditUser(object : UsersRVAdapter.EditUser {
            override fun editUser(user: UserFeatures) {
                val myIntent = Intent(this@RecyclerviewActivity, RegisterActivity::class.java)
                myIntent.putExtra("username", user.getUserName())
                myIntent.putExtra("editMode", true)
                startActivityForResult(myIntent, EDIT_REQUEST)
            }
        })

        myAdapter.setInterDelUser(object : UsersRVAdapter.DelUser {
            override fun delUser(context: Context, user: UserFeatures, position: Int) {
                val mySQLite = SQLiteManager(context)
                mySQLite.setWritable()
                mySQLite.deleteUser(user.getUserName())
                mySQLite.getDb().close()
                myUsers.removeAt(position)
                myAdapter.notifyItemRemoved(position)
            }
        })
    }

    private fun cargarVista() {
        myUsers.clear()
        val mySQLite = SQLiteManager(this)
        mySQLite.setWritable()
        mySQLite.setReadable()
        val myCursor = mySQLite.getUsers()
        while (myCursor.moveToNext()) {
            val auxUserName = myCursor.getString(Integer.parseInt(SQLiteManager.TUsers_USER[1]))
            val auxPassWord = myCursor.getString(Integer.parseInt(SQLiteManager.TUsers_PASSWORD[1]))
            val auxBirthDay = myCursor.getString(
                Integer.parseInt(SQLiteManager.TUsers_BIRTHDATE[1])
            )
            val auxImgProfile =
                getTheImage(myCursor.getBlob(Integer.parseInt(SQLiteManager.TUsers_IMGPROFILE[1])))
            val auxUserRol = myCursor.getString(
                Integer.parseInt(SQLiteManager.TUsers_ROLE[1])
            )
            val auxUser = UserFeatures(
                auxUserName,
                auxPassWord,
                auxBirthDay,
                auxImgProfile,
                auxUserRol
            )
            myUsers.add(auxUser)
        }
        myAdapter.setMyUsers(myUsers)
    }

    private fun getTheImage(imgByte: ByteArray?): Bitmap? {
        return if (imgByte != null) {
            BitmapFactory.decodeByteArray(imgByte, 0, imgByte.size)
        } else null
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                cargarVista()
                Snackbar.make(
                    findViewById(android.R.id.content), "User edited", Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this@RecyclerviewActivity).setTitle("Warning")
            .setMessage("Si sale de esta pantalla, se cerrara la sesion \n¿Esta seguro de salir?")
            .setPositiveButton("SI") { dialogInterface: DialogInterface?, i: Int ->
                super.onBackPressed()
            }.setNegativeButton("NO") { dialogInterface: DialogInterface?, i: Int ->
            }.show()
    }
}