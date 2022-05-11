package com.ihrsachin.pizzashop;

public class CustomPizza {
    private Pizza pizza;
    private int num_of_pizza = 0;

    public CustomPizza(Pizza pizza, int num_of_pizza) {
        this.pizza = pizza;
        this.num_of_pizza = num_of_pizza;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public int getNum_of_pizza() {
        return num_of_pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    public void setNum_of_pizza(int num_of_pizza) {
        this.num_of_pizza = num_of_pizza;
    }
}
