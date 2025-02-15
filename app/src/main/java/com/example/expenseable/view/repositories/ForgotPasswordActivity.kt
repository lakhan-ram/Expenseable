package com.example.expenseable.view.repositories

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.expenseable.R
import com.example.expenseable.databinding.ActivityForgotPasswordBinding
import com.example.expenseable.viewmodel.AuthViewModel

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        authViewModel = AuthViewModel(this)
        binding.btnBackForgotPass.setOnClickListener {
            finish()
        }

        binding.btnSendForgotPass.setOnClickListener {
            val email = binding.etTextEmailForgotPass.text.toString()
            if (email.isEmpty()) {
                binding.textInputLayoutEmailForgotPass.error = "Email is required"
            }
            if (email.isNotEmpty()) {
                authViewModel.resetPassword(email)
            }
        }
    }
}