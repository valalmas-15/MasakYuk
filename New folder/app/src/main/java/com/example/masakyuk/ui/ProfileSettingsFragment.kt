package com.example.masakyuk.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.masakyuk.Login
import com.example.masakyuk.viewmodel.ProfileViewModel
import com.example.masakyuk.MainActivity
import com.example.masakyuk.R
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class ProfileSettingsFragment : Fragment() {
    var iv_upload: ImageView? = null
    var urlgambar: Uri? = null
    var bitmapgambar: Bitmap? = null

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_profile_settings, container, false)

        val session: SharedPreferences = requireActivity().getSharedPreferences("user", Activity.MODE_PRIVATE)
        val id_user = session.getString("id_user", null).toString()

        val db: SQLiteDatabase = requireActivity().openOrCreateDatabase("masakyuk", MODE_PRIVATE, null)
        val query = db.rawQuery("SELECT * FROM user WHERE id_user = '$id_user'", null)
        val btn_hapusAkun:Button = view.findViewById(R.id.btn_hapusAkun)

        if (query.moveToFirst()) { // Pindahkan kursor ke baris pertama (jika ada)
            val email: String = query.getString(1)
            val pass: String = query.getString(2)
            val nama: String = query.getString(3)

            // Pastikan indeks 4 (foto) tidak null
            val pp: ByteArray = query.getBlob(4) ?: byteArrayOf()

            val edtEmail = view.findViewById<EditText>(R.id.edtEmail)
            val edtPassword = view.findViewById<EditText>(R.id.edtPassword)
            val edtNama = view.findViewById<EditText>(R.id.edtNama)
            val btnSave = view.findViewById<Button>(R.id.btnSave)

            iv_upload = view.findViewById(R.id.iv_upload)
            iv_upload?.setOnClickListener {
                openFileChooser()
            }

            edtEmail.setText(email)
            edtNama.setText(nama)

            try {
                val bis = ByteArrayInputStream(pp)
                val gambarbitmap: Bitmap = BitmapFactory.decodeStream(bis)
                iv_upload?.setImageBitmap(gambarbitmap)
            } catch (e: Exception) {
                iv_upload?.setImageResource(R.drawable.defaultpp)
            }

            btnSave.setOnClickListener {
                if (edtEmail.text.toString() != email || edtPassword.text.isNotEmpty() || edtNama.text.toString() != nama || bitmapgambar != null) {
                val passBaru: String = if (edtPassword.text.isNotEmpty()) edtPassword.text.toString() else pass
                val emailBaru: String = edtEmail.text.toString()
                val namaBaru: String = edtNama.text.toString()

                val bos = ByteArrayOutputStream()
                bitmapgambar?.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                val bytearraygambar = bos.toByteArray()

                val sql = "UPDATE user SET email_user=?, pass_user=?, nama_user=?, foto_user=? WHERE id_user='$id_user'"
                val statement = db.compileStatement(sql)
                statement.bindString(1, emailBaru)
                statement.bindString(2, passBaru)
                statement.bindString(3, namaBaru)
                statement.bindBlob(4, bytearraygambar)
                statement.executeUpdateDelete()

                // Restart fragment (refresh data)
                val transaction = requireFragmentManager().beginTransaction()
                transaction.detach(this).attach(this).commit()
                (activity as? MainActivity)?.updateNavHeader()
                } else {
                    Toast.makeText(requireContext(), "Tidak ada perubahan untuk disimpan", Toast.LENGTH_SHORT).show()
                }

            }


        } else {
            // Kursor kosong, lakukan penanganan sesuai
            // Misalnya, tampilkan pesan atau lakukan tindakan yang sesuai
            Toast.makeText(requireContext(), "Profil tidak ditemukan", Toast.LENGTH_SHORT).show()
        }

        btn_hapusAkun.setOnClickListener {
            // Munculkan dialog konfirmasi
            AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Hapus Akun")
                .setMessage("Anda yakin ingin menghapus akun?")
                .setPositiveButton("Ya") { dialog, which ->
                    // User menekan tombol "Ya", eksekusi query penghapusan
                    db.execSQL("DELETE FROM user WHERE id_user = '$id_user'")

                    // Navigasi ke halaman login
                    val balik = Intent(requireContext(), Login::class.java)
                    startActivity(balik)
                    requireActivity().finish()  // Optional: Menutup MainActivity agar tidak dapat dikembalikan
                }
                .setNegativeButton("Tidak", null) // User menekan tombol "Tidak", tidak lakukan apa-apa
                .show()
        }



        query.close() // Jangan lupa menutup kursor setelah digunakan

        return view

    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        pilih_gambar.launch(intent)
    }

    private val pilih_gambar = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val gambardiperoleh = result.data

            if (gambardiperoleh != null) {
                urlgambar = gambardiperoleh.data

                bitmapgambar = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, urlgambar)
                iv_upload?.setImageBitmap(bitmapgambar)
            }
        }
    }
}
