package com.politecnicomalaga.myktcrud.view

import android.app.Activity
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.SQLException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.politecnicomalaga.myktcrud.R
import com.politecnicomalaga.myktcrud.model.SQLiteManager
import com.politecnicomalaga.myktcrud.model.UserFeatures
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var textInputUser: TextInputLayout
    private lateinit var textInputPassword: TextInputLayout
    private lateinit var textInputBirthday: TextInputLayout
    private lateinit var textInputUserGroup: TextInputLayout
    private lateinit var imgProfile: ImageView
    private lateinit var btnGetImg: Button
    private lateinit var btnSaveUser: Button
    lateinit var myImageBitMap: Bitmap
    lateinit var myImageByteArray: ByteArray

    val CAMERA_PERMISSION_REQUEST = 1
    val CAMERA_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        textInputUser = findViewById(R.id.txtFldUser)
        textInputPassword = findViewById(R.id.txtFldPassWord)
        textInputBirthday = findViewById(R.id.txtFldBirthday)
        textInputUserGroup = findViewById(R.id.txtFldUserGroup)
        imgProfile = findViewById(R.id.imgProfile)
        btnGetImg = findViewById(R.id.btnAddImg)
        btnSaveUser = findViewById(R.id.btnSaveUser)

        val myIntent = intent
        val myBundle = myIntent.extras
        lateinit var auxUsername: String
        val mySQLite = SQLiteManager(this@RegisterActivity)

        if (myBundle != null) {
            auxUsername = myBundle.getString("username").toString()
            mySQLite.setWritable()
            val myCursor = mySQLite.getOneUser(auxUsername)
            mySQLite.getDb().close()
            if (myCursor.moveToNext()) {
                auxUsername = myCursor.getString(Integer.parseInt(SQLiteManager.TUsers_USER[1]))
                textInputUser.editText?.setText(myCursor.getString(Integer.parseInt(SQLiteManager.TUsers_USER[1])))
                textInputPassword.editText?.setText(
                    myCursor.getString(
                        Integer.parseInt(
                            SQLiteManager.TUsers_PASSWORD[1]
                        )
                    )
                )
                textInputBirthday.editText?.setText(
                    myCursor.getString(
                        Integer.parseInt(
                            SQLiteManager.TUsers_BIRTHDATE[1]
                        )
                    )
                )
                if (myCursor.getBlob(Integer.parseInt(SQLiteManager.TUsers_IMGPROFILE[1])) != null) {
                    imgProfile.setImageBitmap(
                        getTheImage(
                            myCursor.getBlob(
                                Integer.parseInt(
                                    SQLiteManager.TUsers_IMGPROFILE[1]
                                )
                            )
                        )
                    )
                }
                textInputUserGroup.editText?.setText(
                    myCursor.getString(
                        Integer.parseInt(
                            SQLiteManager.TUsers_ROLE[1]
                        )
                    )
                )
            }
        }

        textInputUser.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    textInputUser.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        textInputPassword.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    textInputPassword.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build()
        textInputBirthday.editText?.setOnClickListener {
            if (!datePicker.isAdded) {
                datePicker.show(supportFragmentManager, "tag")
            }
        }

        datePicker.addOnPositiveButtonClickListener {
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
            val date = Date(it)
            textInputBirthday.editText?.setText(simpleDateFormat.format(date))
        }

        mySQLite.setWritable()
        val myCursor = mySQLite.getGroups()
        val options = arrayOfNulls<String>(myCursor.count)
        var cont = 0
        while (myCursor.moveToNext()) {
            options[cont] = myCursor.getString(0)
            cont++
        }
        mySQLite.getDb().close()
        textInputUserGroup.editText?.setOnClickListener {
            MaterialAlertDialogBuilder(it.context).setTitle("Choose Rol")
                .setSingleChoiceItems(options, -1, ({ dialogInterface: DialogInterface, i: Int ->
                    textInputUserGroup.editText!!.setText(options[i])
                })).setPositiveButton("Accept") { dialogInterface: DialogInterface?, i: Int ->
                    textInputUserGroup.error = null
                }.setNegativeButton("Cancel") { dialogInterface: DialogInterface?, i: Int ->
                    textInputUserGroup.editText!!.text.clear()
                }.show()
        }

        btnGetImg.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@RegisterActivity, android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST
                )
            }
        }

        btnSaveUser.setOnClickListener {
            if (myBundle != null) {
                if (myBundle.getBoolean("editMode")) {
                    if (textInputUser.editText?.text.isNullOrEmpty()) {
                        textInputUser.error = "Invalid User"
                    } else if (textInputPassword.editText?.text.isNullOrEmpty()) {
                        textInputPassword.error = "Incorrect Password"
                    } else if (textInputBirthday.editText?.text.isNullOrEmpty()) {
                        textInputPassword.error = "Set a Date"
                    } else if (textInputUserGroup.editText?.text.isNullOrEmpty()) {
                        textInputPassword.error = "Choose a Rol"
                    } else {
                        lateinit var auxUser: UserFeatures
                        auxUser.username = textInputUser.editText?.text.toString()
                        auxUser.password = textInputPassword.editText?.text.toString()
                        auxUser.birthday = textInputBirthday.editText?.text.toString()
                        auxUser.imgProfile = myImageBitMap
                        auxUser.userRol = textInputUserGroup.editText?.text.toString()
                        editUser(auxUser, auxUsername)

                        val result = Intent(this@RegisterActivity, RecyclerviewActivity::class.java)
                        setResult(RESULT_OK, result)
                        finish()
                    }
                }
            } else {
                addUser(it)
                val result = Intent(this@RegisterActivity, RecyclerviewActivity::class.java)
                setResult(RESULT_OK, result)
                finish()
            }
        }

    }

    private fun editUser(auxUser: UserFeatures, auxUsername: String) {
        val mySQLite = SQLiteManager(this@RegisterActivity)
        mySQLite.setWritable()

        val values = ContentValues()
        values.put(SQLiteManager.TUsers_USER[0], auxUser.username)
        values.put(SQLiteManager.TUsers_PASSWORD[0], auxUser.password)
        values.put(SQLiteManager.TUsers_BIRTHDATE[0], auxUser.birthday)
        values.put(SQLiteManager.TUsers_IMGPROFILE[0], myImageByteArray)
        values.put(SQLiteManager.TUsers_ROLE[0], auxUser.userRol)
        mySQLite.updateUser(values, auxUsername)
        mySQLite.getDb().close()
    }

    private fun addUser(it: View) {
        if (textInputUser.editText?.text.isNullOrEmpty()) {
            textInputUser.error = "Invalid User"
        } else if (textInputPassword.editText?.text.isNullOrEmpty()) {
            textInputPassword.error = "Incorrect Password"
        } else if (textInputBirthday.editText?.text.isNullOrEmpty()) {
            textInputPassword.error = "Set a Date"
        } else if (textInputUserGroup.editText?.text.isNullOrEmpty()) {
            textInputPassword.error = "Choose a Rol"
        } else {
            val mySQLite = SQLiteManager(this@RegisterActivity)
            mySQLite.setWritable()
            val myCursor = mySQLite.getOneUser(textInputUser.editText!!.text.toString())
            if (myCursor.moveToNext()) {
                Snackbar.make(it, "This user already exist", Snackbar.LENGTH_LONG).show()
            } else {
                try {
                    val values = ContentValues()
                    values.put(
                        SQLiteManager.TUsers_USER[0], textInputUser.editText!!.text.toString()
                    )
                    values.put(
                        SQLiteManager.TUsers_PASSWORD[0],
                        textInputPassword.editText!!.text.toString()
                    )
                    values.put(
                        SQLiteManager.TUsers_BIRTHDATE[0],
                        textInputBirthday.editText!!.text.toString()
                    )
                    values.put(SQLiteManager.TUsers_IMGPROFILE[0], myImageByteArray)
                    values.put(
                        SQLiteManager.TUsers_ROLE[0], textInputUserGroup.editText!!.text.toString()
                    )
                    mySQLite.insertUser(values)
                    mySQLite.getDb().close()
                } catch (e: SQLException) {
                    Snackbar.make(it, "Error at insert data", Snackbar.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun getTheImage(imgByte: ByteArray?): Bitmap? {
        return if (imgByte != null) {
            BitmapFactory.decodeByteArray(imgByte, 0, imgByte.size)
        } else null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(
                Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE
                ), CAMERA_REQUEST
            )
        } else {
            requestPermissions(
                arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                myImageBitMap = data.extras?.get("data") as Bitmap
                lateinit var stream: ByteArrayOutputStream
                myImageBitMap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                myImageByteArray = stream.toByteArray()
                imgProfile.setImageBitmap(myImageBitMap)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        MenuInflater(this@RegisterActivity).inflate(R.menu.menu_options, menu)
        val myItem: MenuItem = menu.findItem(R.id.Menu_AboutUs)
        myItem.isVisible = false
        invalidateOptionsMenu()
        val myIntent = intent
        val myBundle = myIntent.extras
        if (myBundle != null) {
            if (myBundle.getBoolean("editMode")) {
                myItem.isVisible = true
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.Menu_AboutUs) {
            val bottomSheetDialog = BottomSheetDialog(
                this@RegisterActivity,
                com.google.android.material.R.style.Theme_Design_BottomSheetDialog
            )
            val bottomSheetView: View = LayoutInflater.from(this@RegisterActivity)
                .inflate(R.layout.buttonsheet_items, findViewById(R.id.buttonSheepLL))
            val lyt: LinearLayout = bottomSheetView.findViewById(R.id.buttonSheepLL)
            BottomSheetBehavior.from(lyt).state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()

            val txtDel = bottomSheetView.findViewById<TextView>(R.id.txtViewDel)
            txtDel.setOnClickListener {
                bottomSheetDialog.dismiss()
                val mySQLite = SQLiteManager(this@RegisterActivity)
                mySQLite.setWritable()
                mySQLite.deleteUser(textInputUser.editText!!.text.toString())
                mySQLite.getDb().close()
                val result = Intent(this@RegisterActivity, RecyclerviewActivity::class.java)
                setResult(RESULT_OK, result)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}