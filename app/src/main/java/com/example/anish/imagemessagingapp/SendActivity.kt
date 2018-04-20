package com.example.anish.imagemessagingapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.FontRequest
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.google.firebase.storage.UploadTask
import com.google.android.gms.tasks.OnSuccessListener
import android.support.annotation.NonNull
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.example.anish.imagemessagingapp.R.id.imageView
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*


class SendActivity : AppCompatActivity() {

    var createSnapImageView: ImageView? = null
    var messageEditText: EditText? = null
    val imageName = UUID.randomUUID().toString()+".jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)

        createSnapImageView = findViewById(R.id.createSnapImageView)
    }

    fun getPhoto(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    fun chooseImageClicked(view: View){
            getPhoto()
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            val selectedImage = data!!.data

            try {
                var bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                var bitmapScaleWidth: Int= (bitmap.width * 0.8).toInt()
                var bitmapScaleHeight: Int = (bitmap.height * 0.8).toInt()
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmapScaleWidth, bitmapScaleHeight, true)
                createSnapImageView?.setImageBitmap(bitmap)

            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun sendClicked(view:View){
        createSnapImageView?.setDrawingCacheEnabled(true)
        createSnapImageView?.buildDrawingCache()
        val bitmap = createSnapImageView?.getDrawingCache()
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var imageRef = FirebaseStorage.getInstance().getReference().child("images")
                .child(imageName)

        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener(OnFailureListener {
            Toast.makeText(this, "Upload Failed!", Toast.LENGTH_SHORT).show()
        }).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
            val downloadUrl = taskSnapshot.downloadUrl
            Log.i("url", downloadUrl.toString())
        })
    }
}

