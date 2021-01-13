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
import com.google.android.material.snackbar.Snackbar;


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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                double userBMR = 0;
                double caloriesNeed = 0;
                double minCarbohydratesNeed = 0;
                double maxCarbohydratesNeed = 0;
                double minProteinsNeed = 0;
                double maxProteinsNeed = 0;
                double minFatsNeed = 0;
                double maxFatsNeed = 0;

                editor.putString("userName", userName.getText().toString());
                editor.putInt("userAge", Integer.parseInt(userAge.getText().toString()));
                editor.putString("userWeightInKgs", userWeight.getText().toString().trim());
                editor.putString("userHeightInMeters", userHeight.getText().toString().trim());
                editor.putFloat("userWeight", (float) (Float.parseFloat(userWeight.getText().toString().trim()) * 2.204622));
                editor.putFloat("userHeight", (float) (Float.parseFloat(userHeight.getText().toString().trim()) * 39.3701));


                editor.apply();
                if (sharedPreferences.getInt("userGenderPos", 0) == 0) {
                    userBMR = 66 + (6.3 * sharedPreferences.getFloat("userWeight", 0) + (12.9 * sharedPreferences.getFloat("userHeight", 0)) - (6.8 * sharedPreferences.getInt("userAge", 0)));
                } else if (sharedPreferences.getInt("userGenderPos", 0) == 1) {
                    userBMR = 655 + (4.3 * sharedPreferences.getFloat("userWeight", 0) + (4.7 * sharedPreferences.getFloat("userHeight", 0)) - (4.7 * sharedPreferences.getInt("userAge", 0)));
                }

                switch (sharedPreferences.getInt("userActivityLevelPos", 0)) {
                    case 0:
                        caloriesNeed = userBMR * 1.2;
                        break;
                    case 1:
                        caloriesNeed = userBMR * 1.375;
                        break;
                    case 2:
                        caloriesNeed = userBMR * 1.55;
                        break;
                    case 3:
                        caloriesNeed = userBMR * 1.725;
                        break;
                }
                editor.putInt("userBMR", (int) userBMR);
                editor.putInt("caloriesNeed", (int) caloriesNeed);


                minCarbohydratesNeed = 0.45 * caloriesNeed / 4.0;
                maxCarbohydratesNeed = 0.65 * caloriesNeed / 4.0;
                minProteinsNeed = 0.1 * caloriesNeed / 4.0;
                maxProteinsNeed = 0.35 * caloriesNeed / 4.0;
                minFatsNeed = 0.2 * caloriesNeed / 9.0;
                maxFatsNeed = 0.35 * caloriesNeed / 9.0;
                editor.putInt("minCarbohydratesNeed", (int) minCarbohydratesNeed);
                editor.putInt("maxCarbohydratesNeed", (int) maxCarbohydratesNeed);
                editor.putInt("minProteinsNeed", (int) minProteinsNeed);
                editor.putInt("maxProteinsNeed", (int) maxProteinsNeed);
                editor.putInt("minFatsNeed", (int) minFatsNeed);
                editor.putInt("maxFatsNeed", (int) maxFatsNeed);

                editor.putInt("userGenderPos", userGender.getSelectedItemPosition());
                editor.putInt("userActivityLevelPos", userActivityLevel.getSelectedItemPosition());

                editor.apply();
                restoreInfo(sharedPreferences);
                Snackbar.make(v, "Saved Successfully!", Snackbar.LENGTH_SHORT).show();
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