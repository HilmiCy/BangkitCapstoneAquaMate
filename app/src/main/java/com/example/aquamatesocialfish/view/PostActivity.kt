package com.example.aquamatesocialfish.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.aquamatesocialfish.HomeActivity
import com.example.aquamatesocialfish.databinding.ActivityPostBinding
import com.example.aquamatesocialfish.models.PostUserModel
import com.example.aquamatesocialfish.models.UserModel
import com.example.aquamatesocialfish.utils.POST
import com.example.aquamatesocialfish.utils.USER_AQUAMATE_FOLDER_POST
import com.example.aquamatesocialfish.utils.USER_COLLECTION
import com.example.aquamatesocialfish.utils.uploadImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding

    private var urlImage: String? = null

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            showProgressBar(true)
            uploadImage(uri, USER_AQUAMATE_FOLDER_POST) { Url ->
                showProgressBar(false)
                if (Url != null) {
                    binding.IvPreview.setImageURI(uri)
                    urlImage = Url
                    binding.IvPreview.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengatur toolbar
        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Menambahkan listener untuk tombol back di toolbar
        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }

        loadUserProfile()

        binding.IvPreview.visibility = View.GONE

        binding.btnAddImage.setOnClickListener {
            launcher.launch("image/*")
        }

        // Listener untuk tombol batal
        binding.btnCancel.setOnClickListener {
            startActivity(Intent(this@PostActivity, HomeActivity::class.java))
            finish()
        }

        // Listener untuk tombol post
        binding.btnPost.setOnClickListener {
            Firebase.firestore.collection(USER_COLLECTION).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
                val user = it.toObject<UserModel>()

                val post : PostUserModel = PostUserModel(
                    contentUrl = urlImage!!,
                    contentCaption = binding.etCaption.text.toString(),
                    uid = Firebase.auth.currentUser!!.uid ,
                    time = System.currentTimeMillis().toString())

                Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {
                    Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document().set(post).addOnSuccessListener {
                        startActivity(Intent(this@PostActivity, HomeActivity::class.java))
                        finish()
            }

                }
            }
        }
    }

    private fun loadUserProfile() {
        Firebase.firestore.collection(USER_COLLECTION)
            .document(Firebase.auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val userAquamate = documentSnapshot.toObject<UserModel>()
                if (userAquamate != null) {
                    userAquamate.image?.let { imageUrl ->
                        if (imageUrl.isNotEmpty()) {
                            Picasso.get().load(imageUrl).into(binding.imageProfile)
                        }
                    }
                }
                binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showProgressBar(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

}