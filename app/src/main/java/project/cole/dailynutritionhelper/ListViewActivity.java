package project.cole.dailynutritionhelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.lang3.math.NumberUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import project.cole.dailynutritionhelper.data.UserMealDatabaseHandler;
import project.cole.dailynutritionhelper.model.Item;
import project.cole.dailynutritionhelper.ui.RecyclerViewAdapter;

public class ListViewActivity extends AppCompatActivity {
    public static final String tableName = "user_info";

    private AnimationDrawable animationDrawable;

    private Button saveButton;
    private EditText foodItem;
    private EditText itemQuantity;
    private EditText itemWeight;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Item> itemList;
    private UserMealDatabaseHandler userMealDatabaseHandler;
    private FloatingActionButton fab;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private BottomNavigationView bottomNavigationView;
    private TextView headerWelcome;
    private TextView caloriesView;
    private TextView carbohydratesView;
    private TextView proteinsView;
    private TextView fatsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        userMealDatabaseHandler = new UserMealDatabaseHandler(this);
        recyclerView = findViewById(R.id.recycler);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView1);


        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //TODO: ADD LISTENER CODE
                switch (item.getItemId()) {
                    case R.id.action_favorites:
                        Intent intent0 = new Intent(ListViewActivity.this, FavListActivity.class);
                        startActivity(intent0);
                        break;
                    case R.id.main_page:
                        break;
                }
                return false;
            }
        });

        fab = findViewById(R.id.floatingActionButton);
        fab.setAlpha(0.35f);
        // set Background Animation
        MotionLayout thisLayout = (MotionLayout) findViewById(R.id.motionLayout);
//        animationDrawable = (AnimationDrawable) thisLayout.getBackground();
//        animationDrawable.setEnterFadeDuration(5000);
//        animationDrawable.setExitFadeDuration(2000);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();

        itemList = userMealDatabaseHandler.getAllItems();
        recyclerViewAdapter = new RecyclerViewAdapter(this, itemList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePopDialog();
            }
        });

        //set header content

        SharedPreferences sharedPreferences = getSharedPreferences(tableName, MODE_PRIVATE);
        View headerView = findViewById(R.id.header);
        headerWelcome = headerView.findViewById(R.id.header_title);
        caloriesView = headerView.findViewById(R.id.caloriesText);
        carbohydratesView = headerView.findViewById(R.id.carboRange);
        proteinsView = headerView.findViewById(R.id.proteinRange);
        fatsView = headerView.findViewById(R.id.fatsRange);
        String welcomeText = "";
        Calendar rightNow = Calendar.getInstance();
        int currentHourIn24 = rightNow.get(Calendar.HOUR_OF_DAY);
        if (6 <= currentHourIn24 && currentHourIn24 < 12) {
            welcomeText = "Good Morning, ";
        } else if (currentHourIn24 >= 12 && currentHourIn24 < 18) {
            welcomeText = "Good Afternoon, ";
        } else {
            welcomeText = "Good Evening, ";
        }
        headerWelcome.setText(MessageFormat.format("{0}{1}", welcomeText, sharedPreferences.getString("userName", null)));
        carbohydratesView.setText(MessageFormat.format("{0} – {1} g", sharedPreferences.getInt("minCarbohydratesNeed", 0),
                sharedPreferences.getInt("maxCarbohydratesNeed", 0)));
        proteinsView.setText(MessageFormat.format("{0} – {1} g", sharedPreferences.getInt("minProteinsNeed", 0),
                sharedPreferences.getInt("maxProteinsNeed", 0)));
        fatsView.setText(MessageFormat.format("{0} – {1} g", sharedPreferences.getInt("minFatsNeed", 0),
                sharedPreferences.getInt("maxFatsNeed", 0)));

        //TODO:adding intake calories into caloriesView
        int currentCalories = 0;
        caloriesView.setText(MessageFormat.format("Calories Intake: {0} / {1}", currentCalories, sharedPreferences.getInt("caloriesNeed", 0)));

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

        userMealDatabaseHandler.addItem(item);

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

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (animationDrawable != null && !animationDrawable.isRunning()) {
//            animationDrawable.start();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        if (animationDrawable != null && animationDrawable.isRunning()) {
//            animationDrawable.stop();
//        }
//    }
}
