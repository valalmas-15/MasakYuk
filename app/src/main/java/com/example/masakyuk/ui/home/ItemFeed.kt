package com.example.masakyuk.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.masakyuk.R
import com.example.masakyuk.ui.home.DetailResep

class ItemFeed(
    private val context: Context,
    private val idResep: MutableList<String>,
    private val isinamaResep: MutableList<String>,
    private val isifotoResep: MutableList<Bitmap>
) : RecyclerView.Adapter<ItemFeed.ViewHolder>() {

    companion object {
        const val INTENT_EXTRA_ID_RESEP = "id_resep_pilih"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fotoResep: ImageView = itemView.findViewById(R.id.iv_fotoresep)
        val namaResep: TextView = itemView.findViewById(R.id.txt_namaResep)
    }

    override fun getItemCount(): Int {
        return isinamaResep.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.namaResep.text = isinamaResep[position]
        holder.fotoResep.setImageBitmap(isifotoResep[position])

        holder.itemView.setOnClickListener {
            navigateToDetail(idResep[position])
        }
    }

    private fun navigateToDetail(idResep: String) {
        val intent = Intent(context, DetailResep::class.java)
        intent.putExtra(INTENT_EXTRA_ID_RESEP, idResep)
        context.startActivity(intent)
    }
}
