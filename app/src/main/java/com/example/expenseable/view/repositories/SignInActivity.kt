package com.example.expenseable.view.repositories

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.expenseable.R
import com.example.expenseable.databinding.ActivitySignInBinding
import com.example.expenseable.viewmodel.AuthViewModel

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignInBinding.inflate(layoutInflater)
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
        binding.tvSignUpSignIn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignInSignIn.setOnClickListener {
            val email = binding.etTextEmailSignIn.text.toString()
            val password = binding.etTextPasswordSignIn.text.toString()

            if (email.isEmpty() && password.isEmpty()) {
                binding.textInputLayoutEmailSignIn.error = "Email is required"
                binding.textInputLayoutPasswordSignIn.error = "Password is required"
            }
            if (email.isEmpty()) {
                binding.textInputLayoutEmailSignIn.error = "Email is required"
            }
            if (password.isEmpty()) {
                binding.textInputLayoutPasswordSignIn.error = "Password is required"
            }
            if (email.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.signIn(email, password)
                binding.progressBarSignIn.visibility = View.VISIBLE
            }
        }
    }
}