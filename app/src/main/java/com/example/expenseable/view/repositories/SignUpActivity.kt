package com.example.expenseable.view.repositories

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.expenseable.R
import com.example.expenseable.databinding.ActivitySignUpBinding
import com.example.expenseable.viewmodel.AuthViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        authViewModel = AuthViewModel(this)
        authViewModel.authList.observe(this) {
            if (it != null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }

        binding.btnBackSignUp.setOnClickListener {
            finish()
        }

        binding.btnSignUpSignUp.setOnClickListener {
            val name = binding.etTextNameSignUp.text.toString()
            val email = binding.etTextEmailSignUp.text.toString()
            val password = binding.etTextPasswordSignUp.text.toString()
            val confirmPassword = binding.etTextConfirmPasswordSignUp.text.toString()

            if (name.isEmpty() && email.isEmpty() && password.isEmpty() && confirmPassword.isEmpty()) {
                binding.textInputLayoutNameSignUp.error = "Name is required"
                binding.textInputLayoutEmailSignUp.error = "Email is required"
                binding.textInputLayoutPasswordSignUp.error = "Password is required"
                binding.textInputLayoutConfirmPasswordSignUp.error = "Confirm password is required"
            }
            if (name.isEmpty()) {
                binding.textInputLayoutNameSignUp.error = "Name is required"
            }
            if (email.isEmpty()) {
                binding.textInputLayoutEmailSignUp.error = "Email is required"
            }
            if (password.isEmpty()) {
                binding.textInputLayoutPasswordSignUp.error = "Password is required"
            }
            if (confirmPassword.isEmpty()) {
                binding.textInputLayoutConfirmPasswordSignUp.error = "Confirm password is required"
            }
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password != confirmPassword) {
                    binding.textInputLayoutConfirmPasswordSignUp.error = "Passwords do not match"
                } else {
                    authViewModel.signUp(name, email, password)
                    binding.progressBarSignUp.visibility = View.VISIBLE
                }
            }
        }
    }
}