package com.example.anish.imagemessagingapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.example.anish.imagemessagingapp.RegisterActivity
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class MainActivity : AppCompatActivity() {

    internal var mAuth = FirebaseAuth.getInstance()
    internal var usernameEditText: EditText? = null
    internal var passwordEditText: EditText? = null
    internal var incorrectUsernameTextView: TextView? = null
    internal var incorrectPassword1TextView: TextView? = null

    fun registerClick(view: View){

        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)

    }

    fun logIn(view: View){

        if (validate(usernameEditText?.text.toString(), passwordEditText?.text.toString())){
                mAuth.signInWithEmailAndPassword(usernameEditText?.text.toString(),
                        passwordEditText?.text.toString())
                        .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {

                                    val intent = Intent(this, SnapsActivity::class.java)
                                    startActivity(intent)
                                    finish()

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.i("failure", task.exception!!.toString())
                                    Toast.makeText(this@MainActivity,
                                            "Authentication failed.", Toast.LENGTH_SHORT).show()
                                }

                                // ...

                        }

            }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.password1EditText)
        incorrectUsernameTextView = findViewById(R.id.incorrectUsernameTextView)
        incorrectPassword1TextView = findViewById(R.id.incorrectPassword1TextView)

        if (mAuth.currentUser != null){
            val intent = Intent(this, SnapsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun validate(email:String, password: String): Boolean{

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        if (!email.matches(emailPattern.toRegex()) || email.length == 0) {
            incorrectUsernameTextView?.text = "Invalid Email!"
            return false
        }

        if (password.length < 6) {
            incorrectUsernameTextView?.text = ""
            incorrectPassword1TextView?.text = "Password must be 6 characters long!"
            return false
        }

        return true
    }
}
