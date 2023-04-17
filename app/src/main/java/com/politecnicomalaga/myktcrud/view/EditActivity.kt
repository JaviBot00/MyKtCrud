package com.politecnicomalaga.myktcrud.view

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.politecnicomalaga.myktcrud.R
import com.politecnicomalaga.myktcrud.model.MySQLiteManager
import com.politecnicomalaga.myktcrud.model.UserFeatures

class EditActivity : FormActivity() {

    private lateinit var btnSaveUser: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val oldUsername: String = intent.getStringExtra("username").toString()
        val mySQLite = MySQLiteManager(this@EditActivity)
        mySQLite.setWritable()
        val myCursor = mySQLite.getOneUser(oldUsername)
        if (myCursor.moveToNext()) {
            mySQLite.getDb().close()
            textInputUser.editText?.setText(myCursor.getString(Integer.parseInt(MySQLiteManager.TUsers_USER[1])))
            textInputPassword.editText?.setText(
                myCursor.getString(
                    Integer.parseInt(
                        MySQLiteManager.TUsers_PASSWORD[1]
                    )
                )
            )
            textInputBirthday.editText?.setText(
                myCursor.getString(
                    Integer.parseInt(
                        MySQLiteManager.TUsers_BIRTHDATE[1]
                    )
                )
            )
            if (myCursor.getBlob(Integer.parseInt(MySQLiteManager.TUsers_IMGPROFILE[1])) != null) {
                imgProfile.setImageBitmap(
                    getTheImage(
                        myCursor.getBlob(
                            Integer.parseInt(
                                MySQLiteManager.TUsers_IMGPROFILE[1]
                            )
                        )
                    )
                )
            }
            textInputUserGroup.editText?.setText(
                myCursor.getString(
                    Integer.parseInt(
                        MySQLiteManager.TUsers_ROLE[1]
                    )
                )
            )
        }

        btnSaveUser = findViewById(R.id.btnSaveUser)
        btnSaveUser.setOnClickListener {
            if (textInputUser.editText?.text.isNullOrEmpty()) {
                textInputUser.error = "Invalid User"
            } else if (textInputPassword.editText?.text.isNullOrEmpty()) {
                textInputPassword.error = "Incorrect Password"
            } else if (textInputBirthday.editText?.text.isNullOrEmpty()) {
                textInputPassword.error = "Set a Date"
            } else if (textInputUserGroup.editText?.text.isNullOrEmpty()) {
                textInputPassword.error = "Choose a Rol"
            } else {
                val auxUserName = textInputUser.editText?.text.toString()
                val auxPassWord = textInputPassword.editText?.text.toString()
                val auxBirthDay = textInputBirthday.editText?.text.toString()
                val auxImgProfile = myImageBitMap
                val auxUserRol = textInputUserGroup.editText?.text.toString()
                val auxUser = UserFeatures(
                    auxUserName, auxPassWord, auxBirthDay, auxImgProfile, auxUserRol
                )

                mySQLite.setWritable()
                val values = ContentValues()
                values.put(MySQLiteManager.TUsers_USER[0], auxUser.getUserName())
                values.put(MySQLiteManager.TUsers_PASSWORD[0], auxUser.getPassWord())
                values.put(MySQLiteManager.TUsers_BIRTHDATE[0], auxUser.getBirthday())
                values.put(MySQLiteManager.TUsers_IMGPROFILE[0], myImageByteArray)
                values.put(MySQLiteManager.TUsers_ROLE[0], auxUser.getUserRol())
                mySQLite.updateUser(values, oldUsername)
                mySQLite.getDb().close()

                val result = Intent(this@EditActivity, RecyclerviewActivity::class.java)
                setResult(RESULT_OK, result)
                finish()
            }
        }
    }

    private fun getTheImage(imgByte: ByteArray?): Bitmap? {
        return if (imgByte != null) {
            BitmapFactory.decodeByteArray(imgByte, 0, imgByte.size)
        } else null
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        MenuInflater(this@EditActivity).inflate(R.menu.menu_options, menu)
        menu.findItem(R.id.Menu_AboutUs)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.Menu_AboutUs) {
            val bottomSheetDialog = BottomSheetDialog(
                this@EditActivity,
                com.google.android.material.R.style.Theme_Design_BottomSheetDialog
            )
            val bottomSheetView: View = LayoutInflater.from(this@EditActivity)
                .inflate(R.layout.buttonsheet_items, findViewById(R.id.buttonSheepLL))
            val lyt: LinearLayout = bottomSheetView.findViewById(R.id.buttonSheepLL)
            BottomSheetBehavior.from(lyt).state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()

            val txtDel = bottomSheetView.findViewById<TextView>(R.id.txtViewDel)
            txtDel.setOnClickListener {
                bottomSheetDialog.dismiss()
                val mySQLite = MySQLiteManager(this@EditActivity)
                mySQLite.setWritable()
                mySQLite.deleteUser(textInputUser.editText!!.text.toString())
                mySQLite.getDb().close()
                val result = Intent(this@EditActivity, RecyclerviewActivity::class.java)
                setResult(RESULT_OK, result)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}