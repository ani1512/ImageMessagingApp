package com.example.anish.imagemessagingapp


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.view.View
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.nav_header_snaps2.*

import java.util.ArrayList

class SnapsActivity2 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    internal var fragmentTransaction: FragmentTransaction? = null
    internal var userList: ListView? = null
    internal var listAdapter: ArrayAdapter<*>? = null
    internal var emails: ArrayList<String>? = ArrayList()
    internal var keys: ArrayList<String>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snaps2)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)


        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        var user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        var header: View = navigationView.getHeaderView(0)

        var userTextView: TextView = header.findViewById(R.id.userTextView)
        userTextView.setText(user.email)



        userList = findViewById(R.id.userList)
        listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,
                emails)
        userList?.adapter = listAdapter

        FirebaseDatabase.getInstance().getReference().child("users")
                .addChildEventListener(object : ChildEventListener {
                    override fun onCancelled(p0: DatabaseError?) {}
                    override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}
                    override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}
                    override fun onChildRemoved(p0: DataSnapshot?) {}
                    override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                        var email = p0?.child("email")?.value as String
                        emails!!.add(email)
                        keys!!.add(p0.key)
                        Log.i("Key", keys.toString())
                        listAdapter!!.notifyDataSetChanged()
                    }
                })

        userList?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            var intent: Intent = Intent(this, SendActivity::class.java)
            intent.putExtra("key", keys!!.get(position))
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.snaps_activity2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.toHome2) {
            // Handle the camera action
        } else if (id == R.id.toSnap2) {

        }
        else if(id == R.id.toCamera){
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
        else if (id == R.id.logout2) {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
