package com.example.project_management.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import com.example.project_management.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.example.project_management.firebase.FirestoreClass
import com.example.project_management.viewmodel.User
import com.google.android.material.navigation.NavigationView

//BaseActivity(), NavigationView.OnNavigationItemSelectedListener
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    // A global variable for User Name
    private lateinit var mUserName: String

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

        FirestoreClass().signInUser(this@MainActivity)


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

    /**
     * A function to get the current user details from firebase.
     */
    fun updateNavigationUserDetails(user: User) {

        mUserName = user.name

        val navView: NavigationView = findViewById(R.id.nav_view)
        // The instance of the header view of the navigation view.
        val headerView = navView.getHeaderView(0)
        // The instance of the user image of the navigation view.
        val navUserImage: ImageView = headerView.findViewById(R.id.nav_user_image)
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(navUserImage);

        // The instance of the user name TextView of the navigation view.
        val navUsername = headerView.findViewById<TextView>(R.id.tv_username)
        // Set the user name
        navUsername.text = user.name

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
        super.onBackPressed()

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