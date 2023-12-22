package com.example.project_management.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import com.example.project_management.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView

//BaseActivity(), NavigationView.OnNavigationItemSelectedListener
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fabButton: FloatingActionButton = findViewById(R.id.fab_create_board)

        fabButton.setOnClickListener {
            startActivity(Intent(this,
                CreateBoardActivity::class.java))
        }

        setUpActionBar()

        navView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)


    }
    private fun setUpActionBar() {
        toolbar = findViewById(R.id.toolbar_main_activity)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_menu)

        toolbar.setNavigationOnClickListener {
            // Toggle drawer
            toggleDrawer()

        }

    }
    private fun toggleDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout)
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            doubleBackToExit()
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_my_profile ->{
                Toast.makeText(this@MainActivity, "My Profile", Toast.LENGTH_LONG).show()
            }

            R.id.nav_sign_out ->{
                Toast.makeText(this@MainActivity, "Sign out", Toast.LENGTH_LONG).show()
            }

        }


        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }



}