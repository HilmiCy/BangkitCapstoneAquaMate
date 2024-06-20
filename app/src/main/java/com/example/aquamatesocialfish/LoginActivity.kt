package com.example.aquamatesocialfish

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aquamatesocialfish.databinding.ActivityLoginBinding
import com.example.aquamatesocialfish.models.UserModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                binding.emailEditText.error = "Please enter email"
                binding.passwordEditText.error = "Please enter password"
                return@setOnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE

            val user = UserModel(email, password)
            Firebase.auth.signInWithEmailAndPassword(user.email!!, user.password!!)
                .addOnCompleteListener(this) { task ->
                    binding.progressBar.visibility = View.GONE

                    if (task.isSuccessful) {
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            task.exception?.localizedMessage ?: "Authentication failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }
}