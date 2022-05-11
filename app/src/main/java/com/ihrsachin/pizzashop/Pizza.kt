package com.ihrsachin.pizzashop

import java.util.ArrayList
import java.util.HashMap

class Pizza(val isVeg: Boolean, val crust: String, val size: String, val price: Int) {

    init {
        val map: MutableMap<String, ArrayList<String>> = HashMap()
        if (map.containsKey("nc")) {
            map["nc"]!!.add("large")
        } else {
            map["nc"] = ArrayList()
        }
    }
}