package com.example.project_management.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.project_management.R
import com.example.project_management.databinding.ActivitySigninBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : BaseActivity() {
    private lateinit var binding: ActivitySigninBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        auth = FirebaseAuth.getInstance()
        binding = ActivitySigninBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.txtSignUp.setOnClickListener {
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
        }
        binding.btbSignIn.setOnClickListener { signInRegisteredUser() }
    }

    private fun signInRegisteredUser(){
        val email: String = binding.eTxtEmailSignIn.text.toString().trim()
        val password: String = binding.eTxtPassSignIn.text.toString().trim()
        if(validateForm(email, password)){
            showProgressDialog("Please wait...")
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    hideProgressDialog()
                    if(task.isSuccessful){
                        val user = auth.currentUser
                        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                        finish()
                    }else{
                        showErrorSnackBar(task.exception!!.message.toString())
                    }
                }
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                showErrorSnackBar("Please enter an email")
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