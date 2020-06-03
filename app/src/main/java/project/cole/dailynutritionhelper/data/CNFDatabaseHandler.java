package project.cole.dailynutritionhelper.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.Output;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import project.cole.dailynutritionhelper.model.Item;
import project.cole.dailynutritionhelper.util.Constants;

public class CNFDatabaseHandler extends SQLiteOpenHelper {
    private static String DB_NAME = "CNFdb.db";
    private final File DB_FILE;
    private final Context context;
    private static String FOOD_TABLE_NAME = "FOOD_NAME";
    private static String NUTRIENT_TABLE_NAME = "NUTRIENT_AMOUNT";

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
                itemToBePassed.setFoodID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("FoodID"))));
                itemToBePassed.setFoodName(cursor.getString(cursor.getColumnIndex("FoodDescription")));
                results.add(itemToBePassed);
            } while (cursor.moveToNext());
        }
        return results;
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
