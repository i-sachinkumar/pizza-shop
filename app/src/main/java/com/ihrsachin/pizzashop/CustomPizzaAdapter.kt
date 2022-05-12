package com.ihrsachin.pizzashop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class CustomPizzaAdapter(context : Context, messages : ArrayList<CustomPizza>) : ArrayAdapter<CustomPizza>(context, 0, messages){

    val main = MainActivity()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val current : CustomPizza? = getItem(position)
        var listViewItem = convertView

        if (listViewItem == null) {
            listViewItem =
                LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        val nameText : TextView = listViewItem!!.findViewById(R.id.pizza_name)
        val sizeText : TextView = listViewItem.findViewById(R.id.pizza_size)
        val quantityText : TextView = listViewItem.findViewById(R.id.pizza_quantity)
        val priceText : TextView = listViewItem.findViewById(R.id.pizza_price)
        val customiseBtn : Button = listViewItem.findViewById(R.id.btn_customize)

        val currPizza : Pizza = current!!.pizza
        var numPizza : Int = current.num_of_pizza


        customiseBtn.setOnClickListener{
            //TODO
            main.customizePizza(position, numPizza)
        }

        nameText.text = currPizza.crust
        sizeText.text = currPizza.size
        quantityText.text = "quantity: $numPizza"
        priceText.text = "price: ${currPizza.price}"


        return listViewItem
    }
}