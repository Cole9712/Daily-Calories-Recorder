package project.cole.dailynutritionhelper;

import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

public class UserInfoActivity extends AppCompatActivity {
    public static final String tableName = "user_info";
    ConstraintLayout userInputContainer;
    AnimationDrawable animationDrawable;
    EditText userName;
    EditText userAge;
    Spinner userGender;
    Spinner userActivityLevel;
    EditText userWeight;
    EditText userHeight;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        userName = findViewById(R.id.nameInput);
        userAge = findViewById(R.id.ageInput);
        userGender = findViewById(R.id.genderSpinner);
        userActivityLevel = findViewById(R.id.activitySpinner);
        userWeight = findViewById(R.id.weightInput);
        userHeight = findViewById(R.id.heightInput);
        saveButton = findViewById(R.id.saveButton);


        userInputContainer = (ConstraintLayout) findViewById(R.id.loginContainer);
        animationDrawable = (AnimationDrawable) userInputContainer.getBackground();
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.getText().toString().isEmpty()) {
                    Snackbar.make(v, "Please Enter Your Name", Snackbar.LENGTH_SHORT).show();
                } else if (userAge.getText().toString().isEmpty() || userHeight.getText().toString().isEmpty() || userWeight.getText().toString().isEmpty()) {
                    Snackbar.make(v, "Missing Essential Information", Snackbar.LENGTH_SHORT).show();
                } else {
                    saveUserInfo(v);
                }
            }

        });


    }

    private void saveUserInfo(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences(tableName, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        double userBMR = 0;
        double caloriesNeed = 0;
        double carbohydratesNeed = 0;
        double proteinsNeed = 0;
        double fatsNeed = 0;

        editor.putString("userName", userName.getText().toString());
        editor.putInt("userAge", Integer.parseInt(userAge.getText().toString()));
        editor.putFloat("userWeight", (float) (Float.parseFloat(userWeight.getText().toString()) * 2.204622));
        editor.putFloat("userHeight", (float) (Float.parseFloat(userHeight.getText().toString()) * 39.3701));
        userGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt("userGenderPos", position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        userActivityLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt("userActivityLevelPos", position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
//        Snackbar.make(v,"Your BMR is "+ userBMR,Snackbar.LENGTH_SHORT).show();


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }
}
