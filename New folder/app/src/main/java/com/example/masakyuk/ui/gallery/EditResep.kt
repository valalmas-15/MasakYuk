package com.example.masakyuk.ui.gallery

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.masakyuk.MainActivity
import com.example.masakyuk.R
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class EditResep : AppCompatActivity() {
    private var iv_upload: ImageView? = null
    private var urlgambar: Uri? = null
    private var bitmapgambar: Bitmap? = null

    private lateinit var edt_NamaResep: EditText
    private lateinit var edt_bahan: EditText
    private lateinit var edt_langkah: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_resep)

        val btn_balikgallery:Button = findViewById(R.id.btn_balikgallery)
        btn_balikgallery.setOnClickListener {
            finish()
        }

        edt_NamaResep = findViewById(R.id.edt_namaResep)
        edt_bahan = findViewById(R.id.edt_bahan)
        edt_langkah = findViewById(R.id.edt_langkah)
        iv_upload = findViewById(R.id.edt_gambar)
        val btn_edtResep: Button = findViewById(R.id.btn_edtResep)

        val id_pilih: String = intent.getStringExtra("id_pilih").toString()

        val db: SQLiteDatabase = openOrCreateDatabase("masakyuk", MODE_PRIVATE, null)
        val sql = db.rawQuery("SELECT * FROM resep WHERE id_resep='$id_pilih'", null)

        if (sql.moveToFirst()) { // Check if there are results
            val isiNama: String = sql.getString(2)
            val isiBahan: String = sql.getString(4)
            val isiLangkah: String = sql.getString(5)
            val pp: ByteArray = sql.getBlob(3) ?: byteArrayOf()

            edt_NamaResep.setText(isiNama)
            edt_bahan.setText(isiBahan)
            edt_langkah.setText(isiLangkah)

            try {
                val bis = ByteArrayInputStream(pp)
                val gambarbitmap: Bitmap = BitmapFactory.decodeStream(bis)
                iv_upload?.setImageBitmap(gambarbitmap)
            } catch (e: Exception) {
                iv_upload?.setImageResource(R.drawable.images)
            }
        }

        iv_upload?.setOnClickListener {
            openFileChooser()
        }

        btn_edtResep.setOnClickListener {
            val isiNamaResep = edt_NamaResep.text.toString()
            val isiBahanResep = edt_bahan.text.toString()
            val isiLangkahResep = edt_langkah.text.toString()

            if (isiNamaResep.isNotEmpty() && isiBahanResep.isNotEmpty() && isiLangkahResep.isNotEmpty()) {
                val query = "UPDATE resep SET nama_resep=?, bahan=?, langkah=?, foto_resep=? WHERE id_resep='$id_pilih'"
                val statement = db.compileStatement(query)
                statement.clearBindings()
                statement.bindString(1, isiNamaResep)
                statement.bindString(2, isiBahanResep)
                statement.bindString(3, isiLangkahResep)

                // Convert the Bitmap to a byte array
                val bos = ByteArrayOutputStream()
                bitmapgambar?.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                val byteArraygambar = bos.toByteArray()

                statement.bindBlob(4, byteArraygambar)
                statement.executeUpdateDelete()

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("fragmentToLoad", R.id.nav_gallery)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Nama resep, deskripsi, dan langkah tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        pilihGambar.launch(intent)
    }

    private val pilihGambar = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val gambardiperoleh = result.data

            if (gambardiperoleh != null) {
                urlgambar = gambardiperoleh.data
                bitmapgambar = MediaStore.Images.Media.getBitmap(contentResolver, urlgambar)
                iv_upload?.setImageBitmap(bitmapgambar)
            }
        }
    }
}
