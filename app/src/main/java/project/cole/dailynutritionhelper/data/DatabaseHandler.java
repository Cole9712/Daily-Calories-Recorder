package project.cole.dailynutritionhelper.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import project.cole.dailynutritionhelper.model.Item;
import project.cole.dailynutritionhelper.util.Constants;

public class DatabaseHandler extends SQLiteOpenHelper {
    private final Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FOOD_LIST = "CREATE TABLE " + Constants.TABLE_MEALS_NAME + "(" +
                Constants.KEY_ID + " INTEGER PRIMARY KEY," + Constants.KEY_FOOD_NAME + " TEXT," +
                Constants.KEY_QUANTITY + " INTEGER," + Constants.KEY_WEIGHT + " INTEGER," +
                Constants.KEY_DATE + " LONG);";

        db.execSQL(CREATE_FOOD_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_MEALS_NAME);

        onCreate(db);

    }

    // CRUD Operations
    public void addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_FOOD_NAME, item.getFoodName());
        contentValues.put(Constants.KEY_QUANTITY, item.getFoodQuantity());
        contentValues.put(Constants.KEY_WEIGHT, item.getFoodWeightInGrams());
        contentValues.put(Constants.KEY_DATE, java.lang.System.currentTimeMillis());

        db.insert(Constants.TABLE_MEALS_NAME, null, contentValues);

        Log.d("DBHandler", "addItem: ");
        db.close();
    }

    //Get an item
    public Item getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_MEALS_NAME,
                new String[]{Constants.KEY_ID, Constants.KEY_FOOD_NAME, Constants.KEY_QUANTITY, Constants.KEY_WEIGHT, Constants.KEY_DATE},
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) cursor.moveToFirst();
        Item itemToBePassed = new Item();
        if (cursor != null) {
            itemToBePassed.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
            itemToBePassed.setFoodName(cursor.getString(cursor.getColumnIndex(Constants.KEY_FOOD_NAME)));
            itemToBePassed.setFoodQuantity(cursor.getInt(cursor.getColumnIndex(Constants.KEY_QUANTITY)));
            itemToBePassed.setFoodWeightInGrams(cursor.getInt(cursor.getColumnIndex(Constants.KEY_WEIGHT)));
            DateFormat dateFormat = DateFormat.getDateInstance();
            // formate date
            String formatDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE))).getTime()); // Oct 23, 2020

            itemToBePassed.setDateItemAdded(formatDate);
        }
        return itemToBePassed;
    }

    public List<Item> getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Item> itemsToBePassed = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_MEALS_NAME,
                new String[]{Constants.KEY_ID, Constants.KEY_FOOD_NAME, Constants.KEY_QUANTITY, Constants.KEY_WEIGHT, Constants.KEY_DATE},
                null,
                null, null, null, Constants.KEY_ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Item itemToBePassed = new Item();
                itemToBePassed.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                itemToBePassed.setFoodName(cursor.getString(cursor.getColumnIndex(Constants.KEY_FOOD_NAME)));
                itemToBePassed.setFoodQuantity(cursor.getInt(cursor.getColumnIndex(Constants.KEY_QUANTITY)));
                itemToBePassed.setFoodWeightInGrams(cursor.getInt(cursor.getColumnIndex(Constants.KEY_WEIGHT)));
                DateFormat dateFormat = DateFormat.getDateInstance();
                // formate date
                String formatDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE))).getTime()); // Oct 23, 2020
                itemToBePassed.setDateItemAdded(formatDate);

                itemsToBePassed.add(itemToBePassed);
            } while (cursor.moveToNext());
        }
        return itemsToBePassed;
    }

    public int updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_FOOD_NAME, item.getFoodName());
        contentValues.put(Constants.KEY_QUANTITY, item.getFoodQuantity());
        contentValues.put(Constants.KEY_WEIGHT, item.getFoodWeightInGrams());
        contentValues.put(Constants.KEY_DATE, java.lang.System.currentTimeMillis());
        int returnedValue = db.update(Constants.TABLE_MEALS_NAME, contentValues, Constants.KEY_ID + "=?",
                new String[]{String.valueOf(item.getId())});
        db.close();
        return returnedValue;
    }

    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_MEALS_NAME, Constants.KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int getItemCount() {
        String query = "SELECT * FROM " + Constants.TABLE_MEALS_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int returnedValue = cursor.getCount();
        db.close();
        return returnedValue;

    }

}
