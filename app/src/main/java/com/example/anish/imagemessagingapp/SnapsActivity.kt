package com.example.anish.imagemessagingapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_snaps.view.*
import com.example.anish.imagemessagingapp.R.id.navigationView
import com.example.anish.imagemessagingapp.R.id.navigationView


class SnapsActivity : AppCompatActivity() {


    var user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    var navigationView: NavigationView? = null
    var toolbar:Toolbar? = null
    var drawerLayout:DrawerLayout? = null
    var identifierTextView:TextView? = null
    var fragmentTransaction: android.support.v4.app.FragmentTransaction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snaps)

        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu)

        var hView: View = navigationView!!.getHeaderView(0)
        identifierTextView = hView.findViewById(R.id.identifierTextView)
        identifierTextView?.text = user.email

        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction!!.add(R.id.mainContainer, HomeFragment()).commit()
        toolbar?.title = "Home"

        navigationView!!.setNavigationItemSelectedListener(
                NavigationView.OnNavigationItemSelectedListener { menuItem ->

                    when(menuItem.itemId){
                        R.id.toHome-> {
                            fragmentTransaction = supportFragmentManager.beginTransaction()
                            fragmentTransaction!!.replace(R.id.mainContainer, HomeFragment()).
                                    commit()
                            toolbar!!.title = "Home"
                            menuItem.setChecked(true)
                            drawerLayout!!.closeDrawers()
                        }
                        R.id.toSnap->{
                            fragmentTransaction = supportFragmentManager.beginTransaction()
                            fragmentTransaction!!.replace(R.id.mainContainer,
                                    SnapFragment()).commit()
                            toolbar!!.title = "Snaps"
                            menuItem.setChecked(true)
                            drawerLayout!!.closeDrawers()
                        }
                        R.id.toChat->{
                            fragmentTransaction = supportFragmentManager.beginTransaction()
                            fragmentTransaction!!.replace(R.id.mainContainer,
                                    ChatFragment()).commit()
                            toolbar!!.title = "Chats"
                            menuItem.setChecked(true)
                            drawerLayout!!.closeDrawers()
                        }

                        R.id.logout->{
                            FirebaseAuth.getInstance().signOut()
                            menuItem.setChecked(true)
                            drawerLayout!!.closeDrawers()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    true
                })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout?.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
