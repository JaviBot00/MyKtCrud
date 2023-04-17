package com.politecnicomalaga.myktcrud.controller

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.politecnicomalaga.myktcrud.R
import com.politecnicomalaga.myktcrud.model.MySQLiteManager
import com.politecnicomalaga.myktcrud.model.UserFeatures

class UsersRVAdapter(fromActivity: Context, myUsersList: ArrayList<UserFeatures>) :
    RecyclerView.Adapter<UsersRVHolder>() {

    private var myUsers: ArrayList<UserFeatures>
    var myContext: Context
    private lateinit var interEditUser: EditUser
    private lateinit var interDelUser: DelUser

    interface EditUser {
        fun editUser(user: UserFeatures)
    }

    interface DelUser {
        fun delUser(context: Context, user: UserFeatures, position: Int)
    }

    init {
        this.myUsers = myUsersList
        myContext = fromActivity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersRVHolder {
        return UsersRVHolder(
            LayoutInflater.from(myContext).inflate(R.layout.rv_items_list, parent, false),
            this@UsersRVAdapter
        )
    }

    override fun onBindViewHolder(holder: UsersRVHolder, position: Int) {
        val myUser: UserFeatures = myUsers[position]
        val myOptions: Array<String> = MySQLiteManager.TUG_ROLES

        if (myUser.getImgProfile() != null) {
            holder.imgProfile.setImageBitmap(myUser.getImgProfile())
        }
        holder.txtUserName.text = myUser.getUserName()
        holder.txtBirthday.text = myUser.getBirthday()

        holder.btnEdit.setOnClickListener {
            if (interEditUser != null) {
                interEditUser.editUser(myUser)
            }
        }
        holder.btnDel.setOnClickListener {
            interDelUser.delUser(myContext, myUser, position)
        }

        when (myUser.getUserRol()) {
            myOptions[0] -> holder.cardFeatures.setCardBackgroundColor(Color.DKGRAY)
            myOptions[1] -> holder.cardFeatures.setCardBackgroundColor(Color.CYAN)
            myOptions[2] -> holder.cardFeatures.setCardBackgroundColor(Color.parseColor("#FFBB86FC"))
        }
    }

    override fun getItemCount(): Int {
        return myUsers.size
    }

    fun setMyUsers(myUsers: ArrayList<UserFeatures>) {
        this.myUsers = myUsers
        notifyDataSetChanged()
    }

    fun setInterEditUser(interEditUser: EditUser) {
        this.interEditUser = interEditUser
    }

    fun setInterDelUser(interDelUser: DelUser) {
        this.interDelUser = interDelUser
    }

}