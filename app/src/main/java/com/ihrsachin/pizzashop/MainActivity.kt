package com.ihrsachin.pizzashop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLConnection
import java.util.ArrayList
import java.util.HashMap
import android.content.DialogInterface
import android.widget.*
import com.google.firebase.database.*
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    // Api
    val API_URL = "https://625bbd9d50128c570206e502.mockapi.io/api/v1/pizza/1"
    var JSONString = ""

    // component
    lateinit var progressBar: ProgressBar
    lateinit var listView : ListView
    lateinit var addBtn : Button
    lateinit var totalPriceTxt : TextView


    // data structures
    val mCustomPizzas = ArrayList<CustomPizza>()
    val availablePizza : MutableMap<String, ArrayList<String>> = HashMap()


    //Firebase Database
    private lateinit var mFirebaseDatabase : FirebaseDatabase
    private lateinit var messageDatabaseReference: DatabaseReference
    private lateinit var mChildEventListener: ChildEventListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initializing components
        progressBar = findViewById(R.id.progress_circular)
        listView = findViewById(R.id.list_view)
        addBtn = findViewById(R.id.add_btn)
        totalPriceTxt = findViewById(R.id.total_price)

        //initializing database
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        messageDatabaseReference = mFirebaseDatabase.reference.child("CustomPizzas")

        //getting json value
        updateOnMainThread()

        val mCustomPizzaAdapter = CustomPizzaAdapter(this, mCustomPizzas)
        listView.adapter = mCustomPizzaAdapter


        // adding pizza
        addBtn.setOnClickListener{
            val crusts : Array<String> = availablePizza.keys.toTypedArray()
            //val colors = arrayOf("red", "green", "blue", "black")

            val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Pick a Crust")
            builder.setItems(crusts, DialogInterface.OnClickListener { dialog, which ->

                // the user clicked on colors[which]
                val sizes : Array<String> = availablePizza.get(crusts[which])!!.toTypedArray()
                val builder1: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
                builder1.setTitle("Pick a size")
                builder1.setItems(sizes,DialogInterface.OnClickListener { dialog1, which1 ->
                    //Toast.makeText(this, crusts[which] + " " +  sizes[which], Toast.LENGTH_SHORT).show()
                    val root = JSONObject(JSONString)
                    val crustArray : JSONArray = root.getJSONArray("crusts")

                    for(i in 0 until crustArray.length()){
                        val name = crustArray.getJSONObject(i).getString("name")
                        val sizeArray:  JSONArray = crustArray.getJSONObject(i).getJSONArray("sizes")

                        if(name == crusts[which]){
                            for(j in 0 until sizeArray.length()){
                                val size = sizeArray.getJSONObject(j).getString("name")
                                val price = sizeArray.getJSONObject(j).getInt("price")
                                if(sizes[which1] == size){
                                    val pizza : Pizza = Pizza(false,name,size, price)
                                    messageDatabaseReference.push().setValue(CustomPizza(pizza, 1))
                                }
                            }
                        }
                    }
                })
                builder1.show()
            })
            builder.show()
        }

        mChildEventListener  = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val mCustomPizza : CustomPizza = snapshot.getValue(CustomPizza::class.java)!!
                mCustomPizzaAdapter.add(mCustomPizza)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        messageDatabaseReference.addChildEventListener(mChildEventListener)



    }
 /* *********** End of OnCreate() ***************/



    private fun updateOnMainThread(){
        progressBar.visibility = VISIBLE
        CoroutineScope(IO).launch { apiRequest() }
    }

    private suspend fun apiRequest() {
        val result = getStringFromApi() // wait until job is done
        passResultOnMainThread(result)
    }

    private fun getStringFromApi(): String {
        var result = ""

        try {
            val website = URL(API_URL)
            val connection: URLConnection = website.openConnection()
            val `in` = BufferedReader(
                InputStreamReader(
                    connection.getInputStream()))

            result = `in`.readLine()

            `in`.close()
        } catch (e: Exception) {  }


        return result
    }
    private suspend fun passResultOnMainThread(result: String) {
        withContext (Main) {
            progressBar.visibility = GONE
            JSONString = result

            val root = JSONObject(JSONString)
            val crusts : JSONArray = root.getJSONArray("crusts")
            if(root.getBoolean("isVeg")){
                //TODO "VEG"
            }
            else{
                //TODO "NON VEG"
                for(i in 0 until crusts.length()){
                    val name = crusts.getJSONObject(i).getString("name")
                    val sizes:  JSONArray = crusts.getJSONObject(i).getJSONArray("sizes")

                    for(j in 0 until sizes.length()){
                        val size = sizes.getJSONObject(j).getString("name")
                        if(availablePizza.containsKey(name)){
                            availablePizza.get(name)!!.add(size)
                        }
                        else{
                            availablePizza.putIfAbsent(name, ArrayList(listOf(size)))
                        }
                    }
                }
            }
        }
    }


}