package com.sheoran.authenticationkotlin

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ShowDataActivity : AppCompatActivity() {

    lateinit var lv: ListView
    lateinit var employeeArrayList: ArrayList<String>
    lateinit var userId: String
    lateinit var firebaseDatabase: FirebaseDatabase

    lateinit var reference: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_data)
        employeeArrayList = java.util.ArrayList()
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase!!.getReference("EmployeeInfo")
//        reference= FirebaseDatabase.getInstance().getReference("EmployeeInfo");
        //        reference= FirebaseDatabase.getInstance().getReference("EmployeeInfo");
        initializeListView()

    }

    private fun initializeListView() {
        employeeArrayList!!.clear()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, employeeArrayList!!)
        lv!!.adapter = adapter
        reference!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

//              for(DataSnapshot ds :snapshot.getChildren()){
//
//                  String value = ds.getValue(String.class);
//                  employeeArrayList.add(value);
//                  adapter.notifyDataSetChanged();
//              }
                employeeArrayList!!.add(snapshot.getValue(String::class.java)!!)
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                employeeArrayList!!.remove(snapshot.getValue(String::class.java)!!)
                adapter.notifyDataSetChanged()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}