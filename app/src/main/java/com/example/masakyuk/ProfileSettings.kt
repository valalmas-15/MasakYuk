package com.example.masakyuk

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ProfileSettings : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtNama: EditText
    private lateinit var ivProfileImage: ImageView
    private lateinit var btnUploadPhoto: Button
    private lateinit var btnSave: Button

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)

        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        edtNama = findViewById(R.id.edtNama)
        ivProfileImage = findViewById(R.id.iv_upload)
        btnSave = findViewById(R.id.btnSave)

        btnUploadPhoto.setOnClickListener {
            openFileChooser()
        }

        btnSave.setOnClickListener {
            saveProfile()
        }

        loadProfileData()
    }

    private fun loadProfileData() {
        // Implement logic to load user data from SharedPreferences and display it in the form
        // ...

        // Example:
        val session: SharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
        val email = session.getString("email_user", "")
        val nama = session.getString("nama_user", "")

        edtEmail.setText(email)
        edtNama.setText(nama)
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            // Implement logic to handle the selected image
            // ...

            // Example:
            val selectedImageUri = data.data
            ivProfileImage.setImageURI(selectedImageUri)
        }
    }

    private fun saveProfile() {
        // Implement logic to save the edited profile data to SharedPreferences and update the database
        // ...

        // Example:
        val session: SharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
        val editor = session.edit()
        editor.putString("email_user", edtEmail.text.toString())
        editor.putString("nama_user", edtNama.text.toString())
        editor.apply()

        // Redirect to the profile activity or any other appropriate action
        // ...
    }
}
