package com.example.masakyuk

import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val session: SharedPreferences = getSharedPreferences("user", MODE_PRIVATE)

        if (session.contains("id_user")) {
            // Session exists, redirect to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Optional: close the current activity to prevent going back to it
        } else {
            setContentView(R.layout.activity_login)

            val edt_email: EditText = findViewById(R.id.edt_email)
            val edt_pass: EditText = findViewById(R.id.edt_pass)
            val btn_login: Button = findViewById(R.id.btn_login)
            val btn_daftar: Button = findViewById(R.id.btn_daftar)

            btn_daftar.setOnClickListener {
                val pdh: Intent = Intent(this, SignUp::class.java)
                startActivity(pdh)
            }

            btn_login.setOnClickListener {
                val isi_email: String = edt_email.text.toString()
                val isi_pass: String = edt_pass.text.toString()

                val db: SQLiteDatabase = openOrCreateDatabase("masakyuk", MODE_PRIVATE, null)

                db.use {
                    val query = it.rawQuery(
                        "SELECT * FROM user WHERE email_user = '$isi_email' AND pass_user = '$isi_pass'",
                        null
                    )
                    if (query.moveToFirst()) {
                        val id = query.getString(0)
                        val email = query.getString(1)
                        val pass = query.getString(2)
                        val nama = query.getString(3)

                        val session: SharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
                        val sesi = session.edit()
                        sesi.putString("id_user", id)
                        sesi.putString("email_user", email)
                        sesi.putString("pass_user", pass)
                        sesi.putString("nama_user", nama)
                        sesi.apply()

                        val pindah = Intent(this, MainActivity::class.java)
                        startActivity(pindah)
                        finish()
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                this@Login,
                                "Email atau Password salah!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }

        }
    }
}
