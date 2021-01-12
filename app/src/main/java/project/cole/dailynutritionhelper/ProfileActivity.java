package project.cole.dailynutritionhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    private EditText userName;
    private EditText userAge;
    private Spinner userGender;
    private Spinner userActivityLevel;
    private EditText userWeight;
    private EditText userHeight;
    private Button restoreButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView3);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //TODO: ADD LISTENER CODE
                switch (item.getItemId()) {
                    case R.id.action_favorites:
                        Intent intent0 = new Intent(ProfileActivity.this, FavListActivity.class);
                        startActivity(intent0);
                        break;
                    case R.id.main_page:
                        Intent intent1 = new Intent(ProfileActivity.this, ListViewActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.action_me:
                        break;
                }
                return false;
            }
        });

        userName = findViewById(R.id.nameChange);
        userGender = findViewById(R.id.genderChange);
        userActivityLevel = findViewById(R.id.activityChange);
        userAge = findViewById(R.id.ageChange);
        userWeight = findViewById(R.id.weightChange);
        userHeight = findViewById(R.id.heightChange);
        restoreButton = findViewById(R.id.restoreBtn);
        saveButton = findViewById(R.id.changeSave);

        restoreInfo(sharedPreferences);

        restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restoreInfo(sharedPreferences);
            }
        });


    }

    private void restoreInfo(SharedPreferences sharedPreferences) {
        userName.setText(sharedPreferences.getString("userName", "Null"));
        userAge.setText(String.format("%d", sharedPreferences.getInt("userAge", 10)));
        userWeight.setText(sharedPreferences.getString("userWeightInKgs", "Null"));
        userHeight.setText(sharedPreferences.getString("userHeightInMeters", "Null"));
        userGender.setSelection(sharedPreferences.getInt("userGenderPos", 0));
        userActivityLevel.setSelection(sharedPreferences.getInt("userActivityLevelPos", 0));
    }
}