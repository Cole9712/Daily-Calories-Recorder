package project.cole.dailynutritionhelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

import project.cole.dailynutritionhelper.data.DatabaseHandler;
import project.cole.dailynutritionhelper.model.Item;
import project.cole.dailynutritionhelper.ui.RecyclerViewAdapter;

public class ListViewActivity extends AppCompatActivity {
    private Button saveButton;
    private EditText foodItem;
    private EditText itemQuantity;
    private EditText itemWeight;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Item> itemList;
    private DatabaseHandler databaseHandler;
    private FloatingActionButton fab;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        databaseHandler = new DatabaseHandler(this);
        recyclerView = findViewById(R.id.recycler);
        fab = findViewById(R.id.floatingActionButton);
        fab.setAlpha(0.35f);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();

        itemList = databaseHandler.getAllItems();
        recyclerViewAdapter = new RecyclerViewAdapter(this, itemList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePopDialog();
            }
        });

    }

    private void generatePopDialog() {
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

    private void saveItem(View v) {
        Item item = new Item();
        item.setFoodName(foodItem.getText().toString().trim());
        item.setFoodQuantity(NumberUtils.toInt(itemQuantity.getText().toString().trim()));
        item.setFoodWeightInGrams(NumberUtils.toInt(itemWeight.getText().toString().trim()));

        databaseHandler.addItem(item);

        Snackbar.make(v, "Food Information Saved", Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
                startActivity(new Intent(ListViewActivity.this, ListViewActivity.class));
                finish();
            }
        }, 1000);
    }
}
