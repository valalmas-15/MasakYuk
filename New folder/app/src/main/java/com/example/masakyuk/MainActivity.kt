package com.example.masakyuk

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.masakyuk.databinding.ActivityMainBinding
import java.io.ByteArrayInputStream
import android.util.Base64
import androidx.lifecycle.ViewModelProvider
import com.example.masakyuk.viewmodel.ProfileViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var headerNama: TextView
    private lateinit var headerEmail: TextView
    private lateinit var headerPp: ImageView
    private lateinit var btnLogout: Button
    private lateinit var profileViewModel: ProfileViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        setSupportActionBar(binding.appBarMain.toolbar)



        headerNama = binding.navView.getHeaderView(0).findViewById(R.id.nama)
        headerEmail = binding.navView.getHeaderView(0).findViewById(R.id.email)
        headerPp = binding.navView.getHeaderView(0).findViewById(R.id.pp)
        btnLogout = binding.navView.getHeaderView(0).findViewById(R.id.btn_logout)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_user_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val session: SharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
        val id_user = session.getString("id_user", null).toString()

        val db: SQLiteDatabase = openOrCreateDatabase("masakyuk", MODE_PRIVATE, null)
        val query = db.rawQuery("SELECT * FROM user WHERE id_user = '$id_user'", null)

        // Check if the query has rows
        if (query.moveToFirst()) {
            val email: String = query.getString(1)
            val nama: String = query.getString(3)

            // Pastikan indeks 4 (foto) tidak null
            val foto: ByteArray = query.getBlob(4) ?: byteArrayOf()

            headerNama.text = nama
            headerEmail.text = email

            try {
                val bis = ByteArrayInputStream(foto)
                val gambarbitmap: Bitmap = BitmapFactory.decodeStream(bis)
                headerPp.setImageBitmap(gambarbitmap)
            } catch (e: Exception) {
                headerPp.setImageResource(R.drawable.defaultpp)
            }
        } else {
            // Handle the case where no rows are returned
            // You might want to show an error message or take appropriate action
        }

        btnLogout.setOnClickListener {
            logout()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    private fun logout() {
        // Clear the session data
        val session: SharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
        val editor = session.edit()
        editor.clear()
        editor.apply()

        // Redirect to the login activity
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish() // Optional: close the current activity to prevent going back to it using the back button
    }

    public fun updateNavHeader() {
        val session: SharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
        val id_user = session.getString("id_user", null).toString()

        val db: SQLiteDatabase = openOrCreateDatabase("masakyuk", MODE_PRIVATE, null)
        val query = db.rawQuery("SELECT * FROM user WHERE id_user = '$id_user'", null)

        if (query.moveToFirst()) { // Pindahkan kursor ke baris pertama (jika ada)
            val namaUser: String = query.getString(3)
            val emailUser: String = query.getString(1)
            val fotoUser: ByteArray = query.getBlob(4) ?: byteArrayOf()

            val nama: TextView = binding.navView.getHeaderView(0).findViewById(R.id.nama)
            val email: TextView = binding.navView.getHeaderView(0).findViewById(R.id.email)
            val pp: ImageView = binding.navView.getHeaderView(0).findViewById(R.id.pp)

            nama.text = namaUser
            email.text = emailUser

            try {
                if (fotoUser.isNotEmpty()) {
                    val fotoByteArray = BitmapFactory.decodeByteArray(fotoUser, 0, fotoUser.size)
                    pp.setImageBitmap(fotoByteArray)
                } else {
                    pp.setImageResource(R.drawable.defaultpp)
                }
            } catch (e: Exception) {
                pp.setImageResource(R.drawable.defaultpp)
            }
        }
    }



}