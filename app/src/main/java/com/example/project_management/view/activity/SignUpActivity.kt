package com.example.project_management.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.project_management.R
import com.example.project_management.databinding.ActivitySignupBinding
import com.example.project_management.firebase.FirestoreClass
import com.example.project_management.viewmodel.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : BaseActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btbSignUp.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
        }
        binding.txtSignInLink.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
        }
        binding.btbSignUp.setOnClickListener { registerUser() }
    }
    fun userRegisteredSuccess(){
        Toast.makeText(this@SignUpActivity, "You have successfully registered the email", Toast.LENGTH_LONG).show()
        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        finish()
    }

    private fun registerUser(){
        val name: String = binding.eTxtName.text.toString().trim()
        val email: String = binding.eTxtEmail.text.toString().trim()
        val password: String = binding.eTxtPass.text.toString().trim()
        if(validateForm(name, email, password)){
            showProgressDialog("Please wait...")
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    hideProgressDialog()
                    if(task.isSuccessful){
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!
                        val user = User(firebaseUser.uid, name, registeredEmail)
                        FirestoreClass().registerUser(this@SignUpActivity, user)

                    }else{
                        Toast.makeText(this@SignUpActivity, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
        }

    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                showErrorSnackBar("Please enter an email")
                false
            }
            name.isEmpty() -> {
                showErrorSnackBar("Please enter a name")
                false
            }
            password.isEmpty() -> {
                showErrorSnackBar("Please enter a password")
                false
            }
            else -> {
                true
            }
        }
    }

}