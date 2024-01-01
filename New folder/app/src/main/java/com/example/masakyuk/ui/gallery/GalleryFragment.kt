package com.example.masakyuk.ui.gallery

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.masakyuk.databinding.FragmentGalleryBinding
// In GalleryFragment
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.masakyuk.R
import java.io.ByteArrayInputStream

class GalleryFragment : Fragment() {
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    private lateinit var id_user: String
    private lateinit var db: SQLiteDatabase

    private val id_resep: MutableList<String> = mutableListOf()
    private val nama_resep: MutableList<String> = mutableListOf()
    private val foto_resep: MutableList<Bitmap> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.rvResep
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val session: SharedPreferences = requireActivity().getSharedPreferences("user", MODE_PRIVATE)
        id_user = session.getString("id_user", null).toString()

        db = requireContext().openOrCreateDatabase("masakyuk", MODE_PRIVATE, null)
        refreshData()

        val adapter = UserItemEditable(
            requireContext(),
            id_resep,
            nama_resep,
            foto_resep
        ) { id -> deleteRecipe(id) }

        recyclerView.adapter = adapter

        val btnTambah: Button = root.findViewById(R.id.btn_tambahResep)
        btnTambah.setOnClickListener {
            val intent = Intent(requireContext(), TambahResep::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        db.close() // Close the database connection in onDestroyView
    }

    fun refreshData() {
        id_resep.clear()
        nama_resep.clear()
        foto_resep.clear()

        val ambilData = db.rawQuery("SELECT * FROM resep WHERE id_user='$id_user'", null)

        while (ambilData.moveToNext()) {
            id_resep.add(ambilData.getString(0))
            nama_resep.add(ambilData.getString(2))
            val pp: ByteArray = ambilData.getBlob(3) ?: byteArrayOf()

            try {
                val bis = ByteArrayInputStream(pp)
                val gambarbitmap: Bitmap = BitmapFactory.decodeStream(bis)
                foto_resep.add(gambarbitmap)
            } catch (e: Exception) {
                val gambarbitmap: Bitmap = BitmapFactory.decodeResource(
                    requireContext().resources,
                    R.drawable.images
                )
                foto_resep.add(gambarbitmap)
            }
        }

        ambilData.close() // Close the cursor after use

        // Notify that the data has changed
        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun deleteRecipe(id_pilih: String) {
        if (!::db.isInitialized) {
            // Handle the case where db is not initialized
            Log.e("GalleryFragment", "Database not initialized")
            return
        }

        db.execSQL("DELETE FROM resep WHERE id_resep = '$id_pilih'")
        refreshData()
    }

}
