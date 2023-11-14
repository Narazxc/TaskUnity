package com.example.project_management

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// need to change AppCompatActivity to BaseActivity
class MyProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
    }
}