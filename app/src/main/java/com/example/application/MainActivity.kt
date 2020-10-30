package com.example.application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = FirebaseDatabase.getInstance()

        val text = findViewById<TextView>(R.id.textView)
        val name = intent.getStringExtra("name")
        val myRef = database.getReference("users").child(name.toString())

        val update = findViewById<Button>(R.id.update)
        val logout = findViewById<Button>(R.id.logout)

        val nameText = findViewById<EditText>(R.id.name)

        myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    var map = dataSnapshot.value as Map<String,Any>
                    text!!.setText(map["username"].toString())
                }
                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })

        update.setOnClickListener{
            if (TextUtils.isEmpty(nameText.text.toString())){
                Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var updateText = nameText.text.toString()
            var map = mutableMapOf<String,Any>()
            map["username"]=updateText
            myRef.updateChildren(map)
        }

        logout.setOnClickListener {
            val i = Intent(this@MainActivity, Register::class.java)
            startActivity(i)
            finish()
        }
    }
}