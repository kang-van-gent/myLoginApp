package com.example.application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*

class Login : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var valueEventListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val user = findViewById<EditText>(R.id.username)
        val pass = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.loginS)
        val regis = findViewById<Button>(R.id.regisS)

        database = FirebaseDatabase.getInstance().reference.child("users")
        var userList: ArrayList<Users> ?= ArrayList()

        login.setOnClickListener {
            if (TextUtils.isEmpty(user.text.toString())){
                Toast.makeText(applicationContext, "Enter username!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(pass.text.toString())){
                Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            valueEventListener = object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val users = ArrayList<Users>()
                    for (snapshot in dataSnapshot.children) {
                        val post = snapshot.getValue(Users::class.java)
                        users.add(post!!)
                    }
                    userList = users
                    var flag = 0 //เปลี่ยนค่าเป็นหนึ่ง ถ้าพบ user ใน database
                    userList?.forEach {
                        var uId = it.uId
                        if(user.text.toString()==uId && pass.text.toString()==it.password){
                            var id = user.text.toString()
                            flag = 1
                            val intent = Intent(this@Login,MainActivity::class.java)
                            intent.putExtra("name",id)
                            startActivity(intent)
                            finish()
                        }
                    } //forEach
                    if (flag==0)
                        Toast.makeText(this@Login, "Incorrect username or password", Toast.LENGTH_LONG).show()

                } //onDataChange
                override fun onCancelled(error: DatabaseError) {
                }
            } //valueEventListener
            database.addValueEventListener(valueEventListener)
        }

        regis.setOnClickListener {
            val i = Intent(this, Register::class.java)
            startActivity(i)
        }
    }

}