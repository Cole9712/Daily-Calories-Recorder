package project.cole.dailynutritionhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

import project.cole.dailynutritionhelper.data.CNFDatabaseHandler;
import project.cole.dailynutritionhelper.model.Item;
import project.cole.dailynutritionhelper.ui.RecyclerViewAdapter;
import project.cole.dailynutritionhelper.ui.FavRecyclerViewAdapter;

public class FavListActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private RecyclerView listView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private BottomNavigationView bottomNavigationView;
    private CNFDatabaseHandler cnfDatabaseHandler;
    public static final String tableName = "user_info";

    private ArrayList<Item> favArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_list);


        fab = findViewById(R.id.favFab);
        listView = findViewById(R.id.favRecyclerView);
        cnfDatabaseHandler = new CNFDatabaseHandler(this);
        try {
            cnfDatabaseHandler.createDB();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView2);


        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //TODO: ADD LISTENER CODE
                switch (item.getItemId()) {
                    case R.id.action_favorites:
                        break;
                    case R.id.main_page:
                        Intent intent0 = new Intent(FavListActivity.this, ListViewActivity.class);
                        startActivity(intent0);
                        break;
                    case R.id.action_me:
                        Intent intent1 = new Intent(FavListActivity.this, ProfileActivity.class);
                        startActivity(intent1);
                        break;
                }
                return false;
            }
        });

        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));
        favArrayList = cnfDatabaseHandler.getAllFavItems();
        FavRecyclerViewAdapter favRecyclerViewAdapter = new FavRecyclerViewAdapter(this, favArrayList);
        listView.setAdapter(favRecyclerViewAdapter);
        favRecyclerViewAdapter.notifyDataSetChanged();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavListActivity.this, AutoTextActivity.class));
                finish();
            }
        });


    }
}
