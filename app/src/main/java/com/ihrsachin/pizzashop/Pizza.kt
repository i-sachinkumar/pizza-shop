package com.ihrsachin.pizzashop

import java.util.ArrayList
import java.util.HashMap

class Pizza : Comparable<Pizza> {

    var isVeg: Boolean = false
    var crust: String = "unknown"
    var size: String = "unknown"
    var price: Int = 0

    constructor(isVeg: Boolean, crust: String, size: String, price: Int) {
        this.isVeg = isVeg
        this.crust = crust
        this.size = size
        this.price = price
        val map: MutableMap<String, ArrayList<String>> = HashMap()
        if (map.containsKey("nc")) {
            map["nc"]!!.add("large")
        } else {
            map["nc"] = ArrayList()
        }
    }
    constructor(){  }

    override fun compareTo(other: Pizza): Int {
        if(other.crust == this.crust && other.size == this.size) return 0
        return 1
    }
}