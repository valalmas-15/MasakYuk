package com.example.masakyuk.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.masakyuk.R
import com.example.masakyuk.databinding.FragmentHomeBinding
import com.example.masakyuk.ui.gallery.UserItemEditable
import java.io.ByteArrayInputStream

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.rvFeed
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val id_resep: MutableList<String> = mutableListOf()
        val nama_resep: MutableList<String> = mutableListOf()
        val foto_resep: MutableList<Bitmap> = mutableListOf()


        // Initialize your database and perform the query
        val db: SQLiteDatabase = requireContext().openOrCreateDatabase("masakyuk",
            Context.MODE_PRIVATE, null)
        val ambilData = db.rawQuery("SELECT * FROM resep ORDER BY id_resep DESC", null)


        while (ambilData.moveToNext()) {
            id_resep.add(ambilData.getString(0))
            nama_resep.add(ambilData.getString(2))
            val pp: ByteArray = ambilData.getBlob(3) ?: byteArrayOf()

            try {
                val bis = ByteArrayInputStream(pp)
                val gambarbitmap: Bitmap = BitmapFactory.decodeStream(bis)
                foto_resep.add(gambarbitmap)
            } catch (e: Exception) {
                val gambarbitmap:Bitmap = BitmapFactory.decodeResource(this.resources, R.drawable.images)
                foto_resep.add(gambarbitmap)
            }
        }

        val adapter = ItemFeed(requireContext(), id_resep, nama_resep, foto_resep)
        recyclerView.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
