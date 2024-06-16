package com.example.aquamatesocialfish.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.aquamatesocialfish.databinding.FragmentEditProfileBinding
import com.example.aquamatesocialfish.models.UserModel
import com.example.aquamatesocialfish.utils.USER_COLLECTION
import com.example.aquamatesocialfish.utils.uploadImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private var selectedImageUri: Uri? = null
    private var savedFullname: String? = null
    private var savedBio: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        binding.btnChooseImage.setOnClickListener {
            saveCurrentData()
            selectImageFromGallery()
        }

        binding.btnSave.setOnClickListener {
            saveProfile()
        }

        if (savedInstanceState != null) {
            restoreSavedData(savedInstanceState)
        } else {
            loadUserProfile()
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("savedFullname", savedFullname)
        outState.putString("savedBio", savedBio)
    }

    private fun saveCurrentData() {
        savedFullname = binding.etFullname.text.toString()
        savedBio = binding.etbio.text.toString()
    }

    private fun restoreSavedData(savedInstanceState: Bundle) {
        savedFullname = savedInstanceState.getString("savedFullname")
        savedBio = savedInstanceState.getString("savedBio")
        binding.etFullname.setText(savedFullname)
        binding.etbio.setText(savedBio)
    }

    private fun loadUserProfile() {
        val userId = Firebase.auth.currentUser?.uid ?: return

        // Show progress bar
        binding.progressBar.visibility = View.VISIBLE

        Firebase.firestore.collection(USER_COLLECTION).document(userId).get().addOnSuccessListener {
            val user: UserModel? = it.toObject(UserModel::class.java)
            user?.let {
                binding.etUsername.setText(it.name)
                if (savedFullname.isNullOrEmpty()) {
                    binding.etFullname.setText(it.fullname)
                } else {
                    binding.etFullname.setText(savedFullname)
                }
                if (savedBio.isNullOrEmpty()) {
                    binding.etbio.setText(it.bio)
                } else {
                    binding.etbio.setText(savedBio)
                }
                if (!it.image.isNullOrEmpty()) {
                    Picasso.get().load(it.image).into(binding.ivProfile)
                }
            }

            // Hide progress bar
            binding.progressBar.visibility = View.GONE
        }.addOnFailureListener {
            // Handle failure
            binding.progressBar.visibility = View.GONE
            Toast.makeText(context, "Failed to load user profile", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                selectedImageUri = uri
                Picasso.get().load(uri).into(binding.ivProfile)
            }
        }
    }

    private fun saveProfile() {
        val userId = Firebase.auth.currentUser?.uid ?: return
        val name = binding.etUsername.text.toString()
        val fullname = binding.etFullname.text.toString()
        val bio = binding.etbio.text.toString()

        if (name.isEmpty() || fullname.isEmpty() || bio.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val userUpdates = hashMapOf<String, Any>(
            "name" to name,
            "fullname" to fullname,
            "bio" to bio
        )

        // Show progress bar
        binding.progressBar.visibility = View.VISIBLE

        if (selectedImageUri != null) {
            uploadImage(selectedImageUri!!, "profileImages") { imageUrl ->
                if (imageUrl != null) {
                    userUpdates["image"] = imageUrl
                    updateUserProfile(userId, userUpdates)
                } else {
                    // Hide progress bar on image upload failure
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            updateUserProfile(userId, userUpdates)
        }
    }

    private fun updateUserProfile(userId: String, userUpdates: Map<String, Any>) {
        Firebase.firestore.collection(USER_COLLECTION).document(userId)
            .update(userUpdates)
            .addOnSuccessListener {
                // Hide progress bar
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()

                refreshProfileFragment()

                parentFragmentManager.popBackStack()
            }
            .addOnFailureListener { e ->
                // Hide progress bar
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun refreshProfileFragment() {
        val profileFragment = parentFragmentManager.findFragmentByTag("ProfileFragment") as? ProfileFragment
        profileFragment?.loadUserProfile()
    }

    companion object {
        private const val REQUEST_CODE_SELECT_IMAGE = 101
    }
}