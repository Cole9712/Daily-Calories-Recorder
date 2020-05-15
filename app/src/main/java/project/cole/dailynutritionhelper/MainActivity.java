package project.cole.dailynutritionhelper;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;

import project.cole.dailynutritionhelper.data.DatabaseHandler;
import project.cole.dailynutritionhelper.model.Item;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private Button saveButton;
    private EditText foodItem;
    private EditText itemQuantity;
    private EditText itemWeight;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHandler = new DatabaseHandler(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        skipToListActivity();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupDialog();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private void skipToListActivity() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        if (databaseHandler.getItemCount()>=1) {
            startActivity(new Intent(MainActivity.this,ListViewActivity.class));
            finish();
        } else if (sharedPreferences.getString("userName", null) == null) {
            startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
        }
    }

    private void saveItem(View view) {
        // todo: save each item to Database, move to next screen
        Item item = new Item();
        item.setFoodName(foodItem.getText().toString().trim());
        item.setFoodQuantity(NumberUtils.toInt(itemQuantity.getText().toString().trim()));
        item.setFoodWeightInGrams(NumberUtils.toInt(itemWeight.getText().toString().trim()));

        databaseHandler.addItem(item);

        Snackbar.make(view, "Food Information Saved", Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();

                startActivity(new Intent(MainActivity.this, ListViewActivity.class));
                finish();
            }
        }, 1100);

    }

    private void popupDialog() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        foodItem = view.findViewById(R.id.foodItem);
        itemQuantity = view.findViewById(R.id.itemQuantity);
        itemWeight = view.findViewById(R.id.itemWeight);
        saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foodItem.getText().toString().isEmpty()) {
                    Snackbar.make(v, "Please Enter the Food Name", Snackbar.LENGTH_SHORT).show();
                } else if (itemQuantity.getText().toString().isEmpty() && itemWeight.getText().toString().isEmpty()) {
                    Snackbar.make(v,"Please Enter Quantity/Weight",Snackbar.LENGTH_SHORT).show();
                } else {
                    saveItem(v);
                }
            }
        });
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
