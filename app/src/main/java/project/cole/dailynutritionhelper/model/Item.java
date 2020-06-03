package project.cole.dailynutritionhelper.model;

public class Item {
    private int id;
    private String foodName;
    private int foodQuantity;
    private int foodWeightInGrams;
    private String dateItemAdded;
    private int foodID;

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
