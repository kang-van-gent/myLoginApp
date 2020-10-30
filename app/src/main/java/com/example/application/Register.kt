package com.example.application

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class Register : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        database = FirebaseDatabase.getInstance().reference

        val regis = findViewById<Button>(R.id.registerR)
        val signIn = findViewById<Button>(R.id.signInR)

        val username = findViewById<EditText>(R.id.user)
        val password = findViewById<EditText>(R.id.pass)

        regis.setOnClickListener {
            val uId = username?.text.toString()
            val user = Users(uId, username?.text.toString(), password?.text.toString())

            if (TextUtils.isEmpty(username.text.toString())){
                Toast.makeText(applicationContext, "Enter username!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password.text.toString())){
                Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            database.child("users").child(username?.text.toString()).setValue(user).addOnCompleteListener {
                if(!it.isSuccessful){
                    Toast.makeText(this, "User create failed", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this, "User created!!", Toast.LENGTH_LONG).show()
                    val i = Intent(this@Register, Login::class.java)
                    startActivity(i)
                    finish()
                }
            }
        }

        signIn.setOnClickListener {
            val i = Intent(this@Register,Login::class.java)
            startActivity(i)
            finish()
        }
    }
}