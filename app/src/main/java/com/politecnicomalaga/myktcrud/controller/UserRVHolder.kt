package com.politecnicomalaga.myktcrud.controller

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.politecnicomalaga.myktcrud.R

class UserRVHolder(itemView: View, userRVAdapter: UserRVAdapter) : RecyclerView.ViewHolder(itemView) {
    var imgProfile: ImageView = itemView.findViewById(R.id.imgProfile)
    var txtUserName: TextView = itemView.findViewById(R.id.txtUserName)
    var txtBirthday: TextView = itemView.findViewById(R.id.txtBirthday)
    var btnEdit: Button = itemView.findViewById(R.id.btnEdit)
    var btnDel: Button = itemView.findViewById(R.id.btnDel)
    var cardFeatures: CardView = itemView.findViewById(R.id.cardFeatures)
    var myAdapter: UserRVAdapter = userRVAdapter
}