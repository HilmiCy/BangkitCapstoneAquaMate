package com.example.aquamatesocialfish.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.aquamatesocialfish.HomeActivity
import com.example.aquamatesocialfish.databinding.ActivityAllRealsBinding
import com.example.aquamatesocialfish.models.ReelsUserModel
import com.example.aquamatesocialfish.models.UserModel
import com.example.aquamatesocialfish.utils.USER_AQUAMATE_FOLDER_REEL
import com.example.aquamatesocialfish.utils.USER_COLLECTION
import com.example.aquamatesocialfish.utils.VIDIO_REEL
import com.example.aquamatesocialfish.utils.uploadVideoReels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class AllRealsActivity : AppCompatActivity() {
    private lateinit var bindingReels: ActivityAllRealsBinding
    lateinit var progressDialog: ProgressDialog
    private lateinit var urlVidio: String
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadVideoReels(uri, USER_AQUAMATE_FOLDER_REEL, progressDialog ) { Url ->
                if (Url != null) {
                    urlVidio = Url

                } else {
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingReels = ActivityAllRealsBinding.inflate(layoutInflater)
        setContentView(bindingReels.root)

        progressDialog = ProgressDialog(this)

        loadUserProfile()

        bindingReels.btnAddReels.setOnClickListener {
            launcher.launch("video/*")
        }

        bindingReels.btnCancel.setOnClickListener {
            startActivity(Intent(this@AllRealsActivity, HomeActivity::class.java))
            finish()
        }

        bindingReels.btnPost.setOnClickListener {
            Firebase.firestore.collection(USER_COLLECTION).document(Firebase.auth.currentUser!!.uid)
                .get().addOnSuccessListener {
                    var userProfile:  UserModel = it.toObject<UserModel>()!!
                    val addReelsPost : ReelsUserModel = ReelsUserModel(urlVidio!!, bindingReels.etCaption.text.toString(), userProfile.image!!)
                    Firebase.firestore.collection(VIDIO_REEL).document().set(addReelsPost).addOnSuccessListener {
                        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + VIDIO_REEL).document().set(addReelsPost)
                        startActivity(Intent(this@AllRealsActivity, HomeActivity::class.java))
                        finish()
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
                            Picasso.get().load(imageUrl).into(bindingReels.imageProfile)
                        }
                    }
                }
                bindingReels.progressBar.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                bindingReels.progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
    }
    private fun showProgressBar(show: Boolean) {
        bindingReels.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}