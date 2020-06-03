package project.cole.dailynutritionhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

import project.cole.dailynutritionhelper.data.CNFDatabaseHandler;
import project.cole.dailynutritionhelper.model.AutoText;
import project.cole.dailynutritionhelper.model.Item;

public class FavListActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private ListView listView;
    private BottomNavigationView bottomNavigationView;

    private ArrayList<Item> favArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_list);
        fab = findViewById(R.id.floatingActionButton);
        listView = findViewById(R.id.favListView);
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

                }
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavListActivity.this, AutoTextActivity.class));
                finish();
            }
        });


    }
}
