package com.example.project_management.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.project_management.R

// need to change AppCompatActivity to BaseActivity
class MyProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
    }
}