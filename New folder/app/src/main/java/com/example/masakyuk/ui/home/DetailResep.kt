package com.example.masakyuk.ui.home

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.masakyuk.R
import java.io.ByteArrayInputStream

class DetailResep : AppCompatActivity() {
    var detailFoto: ImageView? = null
    private val PICK_IMAGE_REQUEST = 1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_resep)

        val detailNamaResep: TextView = findViewById(R.id.detailNamaResep)
        detailFoto = findViewById(R.id.detailFoto)  // Fix the assignment operator here
        val detail_bahan: TextView = findViewById(R.id.detail_bahan)
        val detailLangkah: TextView = findViewById(R.id.detail_langkah)

        val id_pilih: String = intent.getStringExtra("id_resep_pilih").toString()

        val db: SQLiteDatabase = openOrCreateDatabase("masakyuk", MODE_PRIVATE, null)
        val sql = db.rawQuery("SELECT * FROM resep WHERE id_resep='$id_pilih'", null)

        if (sql.moveToFirst()) { // Check if there are results
            val isiNama: String = sql.getString(2)
            val isiBahan: String = sql.getString(4)
            val isiLangkah: String = sql.getString(5)
            val pp: ByteArray = sql.getBlob(3) ?: byteArrayOf()

            detailNamaResep.text = isiNama
            detail_bahan.text = isiBahan
            detailLangkah.text = isiLangkah

            try {
                val bis = ByteArrayInputStream(pp)
                val gambarbitmap: Bitmap = BitmapFactory.decodeStream(bis)
                detailFoto?.setImageBitmap(gambarbitmap)
            } catch (e: Exception) {
                detailFoto?.setImageResource(R.drawable.images)
            }
        }

        val btn_kembali: Button = findViewById(R.id.btn_balikHome)
        btn_kembali.setOnClickListener {
            finish() // This will close the current activity and go back to the previous one (HomeFragment)
        }

        sql.close()
        db.close()
    }
}
