package com.example.anish.imagemessagingapp

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ReceiveActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var receiveListView: ListView? = null
    var receiveArrayList: ArrayList<String> = ArrayList()
    var adapter: ArrayAdapter<*>? = null
    var snaps: ArrayList<DataSnapshot>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)


        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()


        var user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        var header: View = navigationView.getHeaderView(0)

        var userTextView: TextView = header.findViewById<TextView>(R.id.userTextView)
        userTextView.setText(user.email)

        receiveListView = findViewById(R.id.receiveListView)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,
                receiveArrayList)
        receiveListView!!.adapter = adapter

        FirebaseDatabase.getInstance().getReference().child("users").child(user.uid)
                .child("snaps").addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {}
            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot?) {

                var index = 0
                for(snap: DataSnapshot in snaps!!){
                    if (snap.key == p0?.key){
                        snaps!!.removeAt(index)
                        receiveArrayList.removeAt(index)
                        adapter!!.notifyDataSetChanged()
                        break
                    }
                    index++
                }

            }
            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                receiveArrayList.add(p0?.child("from")?.value as String)
                snaps?.add(p0!!)
                adapter!!.notifyDataSetChanged()
            }

        })

        receiveListView?.onItemClickListener = AdapterView
                .OnItemClickListener { parent, view, position, id ->

                    var snapshot = snaps!![position]

                    var intent = Intent(this, ViewSnapActivity::class.java)

                    intent.putExtra("imageName",
                            snapshot.child("imageName").value as String)
                    intent.putExtra("imageURL", snapshot.child("imageURL").value as String)
                    intent.putExtra("message", snapshot.child("message").value as String)
                    intent.putExtra("snapKey", snapshot.key)

                    startActivity(intent)
                }

    }


    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            val intent = Intent(this, SnapsActivity2::class.java)
            startActivity(intent)
            finish()
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

        val id = item.itemId

        if (id == R.id.toHome2) {
            val intent = Intent(this, SnapsActivity2::class.java)
            startActivity(intent)
            finish()
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
