package com.example.masakyuk.ui.gallery

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.masakyuk.MainActivity
import com.example.masakyuk.R
import java.io.ByteArrayOutputStream

class TambahResep : AppCompatActivity() {
    private var iv_daftar: ImageView? = null
    private var urlgambar: Uri? = null
    private var bitmapgambar: Bitmap? = null

    private lateinit var tmb_resep: EditText
    private lateinit var tmb_bahan: EditText
    private lateinit var tmb_langkah: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tambah_resep)

        tmb_resep = findViewById(R.id.tmb_resep)
        tmb_bahan = findViewById(R.id.tmb_bahan)
        tmb_langkah = findViewById(R.id.tmb_langkah)
        iv_daftar = findViewById(R.id.tmb_gambar)
        val btn_tmbResep: Button = findViewById(R.id.btn_tmbResep)

        iv_daftar?.setOnClickListener {
            openFileChooser()
        }

        btn_tmbResep.setOnClickListener {
            tambahResep()
        }
        val btn_kembali: Button = findViewById(R.id.btn_kembaliGallery)
        btn_kembali.setOnClickListener {
            finish()
        }
    }

    private fun tambahResep() {
        val namaResep = tmb_resep.text.toString().trim()
        val bahanResep = tmb_bahan.text.toString().trim()
        val langkahResep = tmb_langkah.text.toString().trim()

        if (namaResep.isNotEmpty() && bahanResep.isNotEmpty() && langkahResep.isNotEmpty()) {
            val session: SharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
            val idUser = session.getString("id_user", null).toString()

            val db: SQLiteDatabase = openOrCreateDatabase("masakyuk", Context.MODE_PRIVATE, null)

            // Convert the Bitmap to a byte array
            val stream = ByteArrayOutputStream()
            bitmapgambar?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val imageInByte = stream.toByteArray()

            val contentValues = ContentValues()
            contentValues.put("id_user", idUser)
            contentValues.put("nama_resep", namaResep)
            contentValues.put("bahan", bahanResep)
            contentValues.put("langkah", langkahResep)
            contentValues.put("foto_resep", imageInByte) // Store the image byte array in the database

            db.insert("resep", null, contentValues)
            val fragment = GalleryFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment_content_main, fragment)
            transaction.addToBackStack(null)
            transaction.commit()

            fragment.refreshData()
            finish()
        } else {
            Toast.makeText(this, "Nama resep, deskripsi, dan langkah tidak boleh kosong", Toast.LENGTH_SHORT).show()
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
