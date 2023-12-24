package com.example.project_management.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.project_management.R
import com.example.project_management.firebase.FirestoreClass
import com.example.project_management.viewmodel.User

// need to change AppCompatActivity to BaseActivity
class MyProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        FirestoreClass().loadUserData(this)
    }

    fun setUserDataInUI(user: User){
        Glide
            .with(this@MyProfileActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(findViewById(R.id.iv_user_image))

        findViewById<TextView>(R.id.et_name).text = user.name
        findViewById<TextView>(R.id.et_email).text = user.email
        if (user.mobile != 0L){
            findViewById<TextView>(R.id.et_mobile).text = user.mobile.toString()
        }
    }
}