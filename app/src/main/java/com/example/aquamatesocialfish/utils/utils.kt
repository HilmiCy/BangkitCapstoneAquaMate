package com.example.aquamatesocialfish.utils

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

fun uploadImage(uri: Uri, folderName: String, callback : (String?) -> Unit){
    var imgUrl: String? = null
    FirebaseStorage.getInstance().getReference(folderName)
        .child(UUID.randomUUID().toString()).putFile(uri)
        .addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {
                imgUrl = it.toString()
                callback(imgUrl)
            }
        }
}

fun uploadVideoReels(uri: Uri, folderName: String, progressDialog: ProgressDialog, callback : (String?) -> Unit){
    var imgUrl: String? = null
    progressDialog.setTitle("Sedang mengupload video reels . . .")
    progressDialog.show()
    FirebaseStorage.getInstance().getReference(folderName)
        .child(UUID.randomUUID().toString()).putFile(uri)
        .addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {
                imgUrl = it.toString()
                progressDialog.dismiss()
                callback(imgUrl)
            }
        }
        .addOnProgressListener {
            val uploaded : Long = (it.bytesTransferred / it.totalByteCount) * 100
            progressDialog.setMessage("Mengupload video reels . . . $uploaded%")
        }
}