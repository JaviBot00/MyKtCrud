package com.politecnicomalaga.myktcrud.controller

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.politecnicomalaga.myktcrud.R
import com.politecnicomalaga.myktcrud.model.SQLiteManager
import com.politecnicomalaga.myktcrud.model.UserFeatures

class UserRVAdapter(fromActivity: Context, userFeatures: ArrayList<UserFeatures>) :
    RecyclerView.Adapter<UserRVHolder>() {

    var myUsers: ArrayList<UserFeatures>
    var myContext: Context

    interface interEditUser
    interface interDelUser

    init {
        myUsers = userFeatures
        myContext = fromActivity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRVHolder {
        return UserRVHolder(
            LayoutInflater.from(myContext).inflate(R.layout.rv_items_list, parent, false),
            this@UserRVAdapter
        )
    }

    override fun onBindViewHolder(holder: UserRVHolder, position: Int) {
        val myUser: UserFeatures = myUsers[position]
        val myOptions: Array<String> = SQLiteManager.TUG_ROLES

        if (myUser.imgProfile != null) {
            holder.imgProfile.setImageBitmap(myUser.imgProfile)
        }
        holder.txtUserName.text = myUser.username
        holder.txtBirthday.text = myUser.birthday

        when (myUser.userRol) {
            myOptions[0] -> holder.cardFeatures.setCardBackgroundColor(Color.DKGRAY)
            myOptions[1] -> holder.cardFeatures.setCardBackgroundColor(Color.CYAN)
            myOptions[2] -> holder.cardFeatures.setCardBackgroundColor(Color.parseColor("#FFBB86FC"))
        }
    }

    override fun getItemCount(): Int {
        return myUsers.size
    }
}