package com.example.project_management.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.project_management.databinding.ActivityMembersBinding

class MembersActivity : AppCompatActivity() {

    lateinit var binding: ActivityMembersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // init view binding
        binding = ActivityMembersBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}