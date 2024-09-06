package com.example.medicinalplantsinformation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private val REQUEST_CAMERA_PERMISSION = 2
    private val REQUEST_GALLERY_PERMISSION = 3
    private val CAMERA_REQUEST = 4
    private val GALLERY_REQUEST = 5

    //private lateinit var imageViewDisplay: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //imageViewDisplay = findViewById(R.id.imageView_display)

       val Button: Button = findViewById(R.id.button_select_image)
        Button.setOnClickListener {
           showImagePickerDialog()
       }
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Photo")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> {
                    if (checkPermission(android.Manifest.permission.CAMERA)) {
                        openCamera()
                    } else {
                        requestPermission(android.Manifest.permission.CAMERA, REQUEST_CAMERA_PERMISSION)
                    }
                }
                1 -> {
                    if (checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        openGallery()
                    } else {
                        requestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_GALLERY_PERMISSION)
                    }
                }
                2 -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST -> {
                    val photo: Bitmap = data?.extras?.get("data") as Bitmap
                   // imageViewDisplay.setImageBitmap(photo)
                }
                GALLERY_REQUEST -> {
                    val imageUri: Uri? = data?.data
                    if (imageUri != null) {
                        try {
                            val inputStream = contentResolver.openInputStream(imageUri)
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                           // imageViewDisplay.setImageBitmap(bitmap)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                REQUEST_CAMERA_PERMISSION -> openCamera()
                REQUEST_GALLERY_PERMISSION -> openGallery()
            }
        } else {
            // Permission denied, show a message to the user
        }
    }
}
