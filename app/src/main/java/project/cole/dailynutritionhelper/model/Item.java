package project.cole.dailynutritionhelper.model;

import android.app.Application;
import android.content.Context;

import androidx.room.ColumnInfo;

import project.cole.dailynutritionhelper.data.CNFDatabaseHandler;


public class Item extends Application {
    private int id;

    private String foodName;
    private int foodQuantity;

    private int foodWeightInGrams;
    private String dateItemAdded;

    private int foodID;

    private int kcal = 0;

    private float carb = 0;


    private float protein = 0;


    private float fat = 0;

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public float getCarb() {
        return carb;
    }

    public void setCarb(float carb) {
        this.carb = carb;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public Item(int id, String foodName, int foodQuantity, int foodWeightInGrams, String dateItemAdded) {
        this.id = id;
        this.foodName = foodName;
        this.foodQuantity = foodQuantity;
        this.foodWeightInGrams = foodWeightInGrams;
        this.dateItemAdded = dateItemAdded;
    }

    public Item(String foodName, int foodQuantity, int foodWeightInGrams, String dateItemAdded) {
        this.foodName = foodName;
        this.foodQuantity = foodQuantity;
        this.foodWeightInGrams = foodWeightInGrams;
        this.dateItemAdded = dateItemAdded;
    }

    public Item() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(int foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public int getFoodWeightInGrams() {
        return foodWeightInGrams;
    }

    public void setFoodWeightInGrams(int foodWeightInGrams) {
        this.foodWeightInGrams = foodWeightInGrams;
    }

    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }
}
