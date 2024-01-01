package com.example.masakyuk

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream

class SignUp : AppCompatActivity() {

    private var iv_daftar: ImageView? = null
    private var urlgambar: Uri? = null
    private var bitmapgambar: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val db: SQLiteDatabase = openOrCreateDatabase("masakyuk", MODE_PRIVATE, null)
        iv_daftar = findViewById(R.id.iv_daftar)
        val daftarEmail: EditText = findViewById(R.id.daftarEmail)
        val daftarPassword: EditText = findViewById(R.id.daftarPassword)
        val daftarNama: EditText = findViewById(R.id.daftarNama)
        val btn_daftar: Button = findViewById(R.id.btnDaftar)

        iv_daftar?.setOnClickListener {
            openFileChooser()
        }

        btn_daftar.setOnClickListener {
            val bos = ByteArrayOutputStream()
            bitmapgambar?.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            val byteArrayGambar = bos.toByteArray()

            val values = ContentValues()
            values.put("email_user", daftarEmail.text.toString())
            values.put("pass_user", daftarPassword.text.toString())
            values.put("nama_user", daftarNama.text.toString())
            values.put("foto_user", byteArrayGambar)

            db.insert("user", null, values)

            val balik:Intent = Intent(this, Login::class.java)
            startActivity(balik)
        }

        val btn_balik:Button = findViewById(R.id.btn_baliklogin)
        btn_balik.setOnClickListener {
            val balik:Intent = Intent(this, Login::class.java)
            startActivity(balik)
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        pilihGambar.launch(intent)
    }

    private val pilihGambar = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val gambardiperoleh = result.data

            if (gambardiperoleh != null) {
                urlgambar = gambardiperoleh.data
                bitmapgambar = MediaStore.Images.Media.getBitmap(contentResolver, urlgambar)
                iv_daftar?.setImageBitmap(bitmapgambar)
            }
        }
    }
}
