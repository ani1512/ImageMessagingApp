package com.example.anish.imagemessagingapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    internal var mAuth = FirebaseAuth.getInstance()
    internal var emailEditText: EditText? = null
    internal var passwordEditText: EditText? = null
    internal var cpasswordEditText: EditText? = null
    internal var incorrectEmailTextView: TextView? = null
    internal var incorrectPasswordTextView: TextView? = null
    internal var incorrectCPasswordTextView: TextView? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        cpasswordEditText = findViewById(R.id.confirmEditText)
        incorrectEmailTextView = findViewById(R.id.incorrectMailTextView)
        incorrectPasswordTextView = findViewById(R.id.incorrectPasswordTextView)
        incorrectCPasswordTextView = findViewById(R.id.incorrectCPasswordTextView)
    }

    private fun signUp() {
        val intent = Intent(this, SnapsActivity2::class.java)
        startActivity(intent)
    }

    fun registerFunction(view: View) {

        if (validate(emailEditText?.text.toString(),
                passwordEditText?.text.toString(), cpasswordEditText?.text.toString())) {
            mAuth.createUserWithEmailAndPassword(emailEditText?.text.toString(),
                    passwordEditText?.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            FirebaseDatabase.getInstance().getReference().child("users").
                                    child(task.result.user.uid).child("email").
                                    setValue(emailEditText?.text.toString())
                            signUp()

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("failure", task.exception!!.toString())
                            Toast.makeText(this@RegisterActivity,
                                    "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }

                        // ...
                    }
        }

    }

    fun backToLoginFunction(view: View) {
        finish()
    }

    fun validate(email: String, password: String, cpassword: String): Boolean {

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        if (!email.matches(emailPattern.toRegex()) || email.length == 0) {
            incorrectEmailTextView?.text = "Invalid Email!"
            return false
        }

        if (password.length < 6) {
            incorrectEmailTextView?.text = ""
            incorrectPasswordTextView?.text = "Password must be 6 characters long!"
            return false
        }

        if (password != cpassword) {
            incorrectPasswordTextView?.text = ""
            incorrectCPasswordTextView?.text = "Incorrect Password!"
            return false
        }
        incorrectCPasswordTextView?.text = ""
        return true
    }
}
