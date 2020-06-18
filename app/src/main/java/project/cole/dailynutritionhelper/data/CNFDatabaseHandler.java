package project.cole.dailynutritionhelper.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.Output;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.SynchronousQueue;

import project.cole.dailynutritionhelper.model.Item;
import project.cole.dailynutritionhelper.util.Constants;

public class CNFDatabaseHandler extends SQLiteOpenHelper {
    private static String DB_NAME = "CNFdb.db";
    private final File DB_FILE;
    private final Context context;
    private static final String FOOD_TABLE_NAME = "FOOD_NAME";
    private static final String NUTRIENT_TABLE_NAME = "NUTRIENT_AMOUNT";
    private static final String FAV_TABLE_NAME = "FAV_TABLE";

    public CNFDatabaseHandler(@Nullable Context context) {
        super(context, DB_NAME, null, Constants.DATABASE_VERSION);
        assert context != null;
        DB_FILE = context.getDatabasePath(DB_NAME);
        this.context = context;
    }

    public void createDB() throws IOException {
        if (!DB_FILE.exists()) {
            getReadableDatabase();
            close();
            try {
                copyDB();
//                Log.d("CNF", "createDB: ");
            } catch (IOException e) {
                throw new Error("Error Copying DB!");
            }
        }
    }

    private void copyDB() throws IOException {
        int length;
        InputStream inputStream = context.getAssets().open(DB_NAME);
        OutputStream outputStream = new FileOutputStream(DB_FILE);
        byte[] buffer = new byte[1024];

        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    public ArrayList<Item> getAllItems() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Item> results = new ArrayList<>();
        Cursor cursor = db.query(FOOD_TABLE_NAME,
                new String[]{"FoodID", "FoodDescription"},
                null,
                null, null, null, "FoodID DESC");

        if (cursor.moveToFirst()) {
            do {
                Item itemToBePassed = new Item();
                int foodID = Integer.parseInt(cursor.getString(cursor.getColumnIndex("FoodID")));
                itemToBePassed.setFoodID(foodID);
                itemToBePassed.setFoodName(cursor.getString(cursor.getColumnIndex("FoodDescription")));
                results.add(itemToBePassed);
            } while (cursor.moveToNext());
        }
        return results;
    }

    public ArrayList<Item> getAllFavItems() {
        SQLiteDatabase db = getReadableDatabase();
        String CREATE_FAV_TABLE = "CREATE TABLE if not exists " + FAV_TABLE_NAME + "(FoodID INTEGER PRIMARY KEY, FoodName TEXT, FoodKcal TEXT);";
        db.execSQL(CREATE_FAV_TABLE);
        ArrayList<Item> results = new ArrayList<>();
        Cursor cursor = db.query(FAV_TABLE_NAME,
                new String[]{"FoodID", "FoodName", "FoodKcal"},
                null,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Item itemToBePassed = new Item();
                int foodID = Integer.parseInt(cursor.getString(cursor.getColumnIndex("FoodID")));
                itemToBePassed.setFoodID(foodID);
                itemToBePassed.setFoodName(cursor.getString(cursor.getColumnIndex("FoodName")));
                itemToBePassed.setKcal(Integer.parseInt(cursor.getString(cursor.getColumnIndex("FoodKcal"))));
                Log.d("cnfdata", "getAllFavItems: " + itemToBePassed.getFoodName());
                results.add(itemToBePassed);
            } while (cursor.moveToNext());
        }
        return results;
    }

    public String getKCal(int foodID) {
        SQLiteDatabase db = getReadableDatabase();
        String results;
        Cursor cursor = db.query(NUTRIENT_TABLE_NAME,
                new String[]{"NutrientValue"}, "FoodID = ? AND NutrientID = ?",
                new String[]{String.valueOf(foodID), "208"}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            results = cursor.getString(cursor.getColumnIndex("NutrientValue"));

            return results;
        }


        return null;

    }

    public String getProtein(int foodID) {
        SQLiteDatabase db = getReadableDatabase();
        String results;
        Cursor cursor = db.query(NUTRIENT_TABLE_NAME,
                new String[]{"NutrientValue"}, "FoodID = ? AND NutrientID = ?",
                new String[]{String.valueOf(foodID), "203"}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            results = cursor.getString(cursor.getColumnIndex("NutrientValue"));

            return results;
        }


        return null;

    }

    public String getFat(int foodID) {
        SQLiteDatabase db = getReadableDatabase();
        String results;
        Cursor cursor = db.query(NUTRIENT_TABLE_NAME,
                new String[]{"NutrientValue"}, "FoodID = ? AND NutrientID = ?",
                new String[]{String.valueOf(foodID), "204"}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            results = cursor.getString(cursor.getColumnIndex("NutrientValue"));

            return results;
        }


        return null;

    }

    public String getCarb(int foodID) {
        SQLiteDatabase db = getReadableDatabase();
        String results;
        Cursor cursor = db.query(NUTRIENT_TABLE_NAME,
                new String[]{"NutrientValue"}, "FoodID = ? AND NutrientID = ?",
                new String[]{String.valueOf(foodID), "205"}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            results = cursor.getString(cursor.getColumnIndex("NutrientValue"));

            return results;
        }


        return null;

    }

    public void deleteFavItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(FAV_TABLE_NAME, "FoodID=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void addFavItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        String CREATE_FAV_TABLE = "CREATE TABLE if not exists " + FAV_TABLE_NAME + "(FoodID INTEGER PRIMARY KEY, FoodName TEXT, FoodKcal TEXT);";
        db.execSQL(CREATE_FAV_TABLE);
        ContentValues contentValues = new ContentValues();
        contentValues.put("FoodID", item.getFoodID());
        contentValues.put("FoodName", item.getFoodName());
        contentValues.put("FoodKcal", item.getKcal());
        db.insert(FAV_TABLE_NAME, null, contentValues);

        db.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            try {
                copyDB();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
