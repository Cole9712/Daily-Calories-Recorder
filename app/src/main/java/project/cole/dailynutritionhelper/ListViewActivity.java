package project.cole.dailynutritionhelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import project.cole.dailynutritionhelper.data.CNFDatabaseHandler;
import project.cole.dailynutritionhelper.data.UserMealDatabaseHandler;
import project.cole.dailynutritionhelper.model.Item;
import project.cole.dailynutritionhelper.ui.RecyclerViewAdapter;

public class ListViewActivity extends AppCompatActivity {
    public static final String tableName = "user_info";

    private AnimationDrawable animationDrawable;

    private Button saveButton;


    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Item> itemList;
    private UserMealDatabaseHandler userMealDatabaseHandler;
    private FloatingActionButton fab;


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
        SharedPreferences sharedPreferences = getSharedPreferences(tableName, MODE_PRIVATE);
        if (sharedPreferences.getString("userName", null) == null) {
            startActivity(new Intent(ListViewActivity.this, UserInfoActivity.class));
            finish();
        }

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
                    case R.id.action_me:
                        Intent intent1 = new Intent(ListViewActivity.this, ProfileActivity.class);
                        startActivity(intent1);
                        break;
                }
                return false;
            }
        });

        fab = findViewById(R.id.favFab);
//        fab.setAlpha(0.35f);
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


        int currentCalories = 0;
        DateFormat dateFormat = DateFormat.getDateInstance();
        String todayDate = dateFormat.format(new Date((long) java.lang.System.currentTimeMillis()).getTime());
        Log.d("date", "onCreate Current date: " + todayDate);
        for (Item singleItem : itemList) {
            if (singleItem.getDateItemAdded().equals(todayDate)) {
                Log.d("date", "onCreate: " + singleItem.getDateItemAdded());
                currentCalories += (((float) singleItem.getFoodWeightInGrams() / 100.0) * (float) singleItem.getKcal());
            }
        }
        caloriesView.setText(MessageFormat.format("Calories Intake Today: {0} / {1}", currentCalories, sharedPreferences.getInt("caloriesNeed", 0)));

    }

    private void generatePopDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.list_popup, null);
        final AlertDialog alertDialog;
        Button chooseFromButton = view.findViewById(R.id.popupFirstButton);
        Button manualInputButton = view.findViewById(R.id.popupSecondButton);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
        chooseFromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseItem();
                alertDialog.dismiss();
            }
        });

        manualInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputManually();
                alertDialog.dismiss();
            }
        });


//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (foodItem.getText().toString().isEmpty()) {
//                    Snackbar.make(v, "Please Enter the Food Name", Snackbar.LENGTH_SHORT).show();
//                } else if (itemQuantity.getText().toString().isEmpty() && itemWeight.getText().toString().isEmpty()) {
//                    Snackbar.make(v,"Please Enter Quantity/Weight",Snackbar.LENGTH_SHORT).show();
//                } else {
//                    saveItem(v);
//                }
//            }
//        });


    }

    private void inputManually() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.popup, null);
        final AlertDialog alertDialog;
        Button saveButton = view.findViewById(R.id.saveButton);
        final EditText itemWeight = view.findViewById(R.id.itemWeight);
        final EditText itemDesc = view.findViewById(R.id.foodItem);

        final CNFDatabaseHandler cnfDatabaseHandler = new CNFDatabaseHandler(this);

        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemDesc.getText().toString().isEmpty()) {
                    Snackbar.make(v, "Please Enter the Food DESCRIPTION", Snackbar.LENGTH_SHORT).show();
                } else if (itemWeight.getText().toString().isEmpty()) {
                    Snackbar.make(v, "Please Enter the Food WEIGHT", Snackbar.LENGTH_SHORT).show();
                } else {
                    saveItemManually(view, cnfDatabaseHandler);
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
        });
    }

    private void saveItemManually(View view, CNFDatabaseHandler cnfDatabaseHandler) {
        EditText itemProt = view.findViewById(R.id.foodProtein);
        EditText itemKcal = view.findViewById(R.id.foodKcal);
        EditText itemFat = view.findViewById(R.id.foodFat);
        EditText itemCarb = view.findViewById(R.id.foodCarb);
        CheckBox saveForFav = view.findViewById(R.id.checkBox);
        final EditText itemWeight = view.findViewById(R.id.itemWeight);
        final EditText itemDesc = view.findViewById(R.id.foodItem);
        Item item = new Item();
        item.setManualItem(true);
        item.setFoodName(itemDesc.getText().toString().trim());
        item.setFoodWeightInGrams(NumberUtils.toInt(itemWeight.getText().toString().trim()));
//        item.setFoodID(favItem.getFoodID());
        item.setKcal(Integer.parseInt(itemKcal.getText().toString().trim()));
        item.setProtein(Float.parseFloat(itemProt.getText().toString().trim()));
        item.setFat(Float.parseFloat(itemFat.getText().toString().trim()));
        item.setCarb(Float.parseFloat(itemCarb.getText().toString().trim()));
        userMealDatabaseHandler.addItem(item);

//        if (saveForFav.isChecked()) {
//            cnfDatabaseHandler.addFavItem(item);
//        }

        Snackbar.make(view, "Food Information Saved", Snackbar.LENGTH_SHORT).show();

    }

    private void chooseItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.list_choose_popup, null);
        Spinner favSpinner = view.findViewById(R.id.favSpinner);
        final AlertDialog alertDialog;
        Button saveButton = view.findViewById(R.id.saveButton2);
        final EditText itemWeight = view.findViewById(R.id.itemWeight2);

        final CNFDatabaseHandler cnfDatabaseHandler = new CNFDatabaseHandler(this);
        ArrayAdapter<Item> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cnfDatabaseHandler.getAllFavItems());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        favSpinner.setAdapter(arrayAdapter);

        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemWeight.getText().toString().isEmpty()) {
                    Snackbar.make(v, "Please Enter the Food Weight", Snackbar.LENGTH_SHORT).show();
                } else {
                    saveItemOnChoose(view, cnfDatabaseHandler);
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
        });
    }

    private void saveItemOnChoose(View v, CNFDatabaseHandler cnfDatabaseHandler) {
        Spinner spinner = v.findViewById(R.id.favSpinner);
        EditText editText = v.findViewById(R.id.itemWeight2);
        Item favItem = (Item) spinner.getSelectedItem();
        Item item = new Item();
        item.setFoodName(favItem.getFoodName());
//        item.setFoodQuantity(NumberUtils.toInt(itemQuantity.getText().toString().trim()));
        item.setFoodWeightInGrams(NumberUtils.toInt(editText.getText().toString().trim()));
        item.setFoodID(favItem.getFoodID());
        item.setKcal(favItem.getKcal());
        item.setProtein(Float.parseFloat(cnfDatabaseHandler.getProtein(item.getFoodID())));
        item.setFat(Float.parseFloat(cnfDatabaseHandler.getFat(item.getFoodID())));
        item.setCarb(Float.parseFloat(cnfDatabaseHandler.getCarb(item.getFoodID())));

        userMealDatabaseHandler.addItem(item);

        Snackbar.make(v, "Food Information Saved", Snackbar.LENGTH_SHORT).show();

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
