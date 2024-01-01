package com.example.masakyuk.ui.gallery

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.masakyuk.R
import com.example.masakyuk.ui.home.DetailResep
import com.example.masakyuk.ui.home.ItemFeed

class UserItemEditable(
    val ini: Context,
    val id_resep: MutableList<String>,
    val nama: MutableList<String>,
    val foto_resep: MutableList<Bitmap>,
    val onDeleteClick: (String) -> Unit // Add a callback for delete click
) :
    RecyclerView.Adapter<UserItemEditable.ViewHolder>() {

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val listname: TextView = itemView.findViewById(R.id.listName)
        val btn_hpsUserList: Button = itemView.findViewById(R.id.btn_hapus_userlist)
        val btn_edtUserList: Button = itemView.findViewById(R.id.btn_ubah_userlist)
        val foto_resep: ImageView = itemView.findViewById(R.id.listImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.user_item_editable, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return nama.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.listname.text = nama[position]
        holder.foto_resep.setImageBitmap(foto_resep[position])

        holder.itemView.setOnClickListener {
            navigateToDetail(id_resep[position])
        }

        holder.btn_hpsUserList.setOnClickListener {
            AlertDialog.Builder(ini)
                .setTitle("Konfirmasi Hapus Resep")
                .setMessage("Anda yakin ingin menghapus resep ini?")
                .setPositiveButton("Ya") { _, _ ->
                    onDeleteClick.invoke(id_resep[position]) // Invoke the callback on delete confirmation
                }
                .setNegativeButton("Tidak", null)
                .show()
        }

        holder.btn_edtUserList.setOnClickListener {
            Log.d("UserItemEditable", "Selected ID: ${id_resep[position]}")

            val id_pilih: String = id_resep[position]
            val pindah: Intent = Intent(ini, EditResep::class.java)
            pindah.putExtra("id_pilih", id_pilih)
            ini.startActivity(pindah)
        }
    }

    private fun navigateToDetail(idResep: String) {
        val intent = Intent(ini, DetailResep::class.java)
        intent.putExtra(ItemFeed.INTENT_EXTRA_ID_RESEP, idResep)
        ini.startActivity(intent)
    }
}
