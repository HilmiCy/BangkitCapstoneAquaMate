package com.example.aquamatesocialfish

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.aquamatesocialfish.databinding.ActivityRegisterBinding
import com.example.aquamatesocialfish.models.UserModel
import com.example.aquamatesocialfish.utils.USER_AQUAMATE_FOLDER_PROFILE
import com.example.aquamatesocialfish.utils.USER_COLLECTION
import com.example.aquamatesocialfish.utils.uploadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var user: UserModel

    // ActivityResultLauncher for picking image
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            showProgressBar(true)
            uploadImage(uri, USER_AQUAMATE_FOLDER_PROFILE) { imageUrl ->
                showProgressBar(false)
                if (imageUrl == null) {
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                } else {
                    user.image = imageUrl
                    // Display the selected image in ImageView (optional)
                    binding.ivAddimage.setImageURI(uri)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = UserModel()

        binding.btnChooseImage.setOnClickListener {
            // Launch image picker
            launcher.launch("image/*")
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            val fullname = binding.edFullName.text.toString()
            val bio = binding.edBio.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                showProgressBar(true)
                // Create user with email and password
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        showProgressBar(false)
                        if (task.isSuccessful) {
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            if (currentUser != null) {
                                user.name = name
                                user.email = email
                                user.password = password
                                user.fullname = fullname
                                user.bio = bio

                                // Store user data in Firestore
                                Firebase.firestore.collection(USER_COLLECTION)
                                    .document(currentUser.uid)
                                    .set(user)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                                        // Navigate to login screen
                                        navigateToLogin()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Failed to register user: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(this, "Current user not available", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "Registration failed: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        binding.tvLogin.setOnClickListener {
            // Navigate to login screen
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showProgressBar(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}