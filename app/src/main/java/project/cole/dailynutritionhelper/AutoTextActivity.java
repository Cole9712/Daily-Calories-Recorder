package project.cole.dailynutritionhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

import project.cole.dailynutritionhelper.data.CNFDatabaseHandler;
import project.cole.dailynutritionhelper.model.AutoText;
import project.cole.dailynutritionhelper.model.Item;
import project.cole.dailynutritionhelper.ui.AutoTextListViewAdapter;

public class AutoTextActivity extends AppCompatActivity {
    private ArrayList<Item> allFoodList;
    private CNFDatabaseHandler cnfDatabaseHandler;
    private EditText editText;
    private ListView listView;
    private AutoText autoText;
    private AutoTextListViewAdapter autoTextListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_text);
        cnfDatabaseHandler = new CNFDatabaseHandler(this);
        listView = findViewById(R.id.popupListView);
        editText = findViewById(R.id.searchText);
        autoTextListViewAdapter = new AutoTextListViewAdapter(this, null);

        listView.setAdapter(autoTextListViewAdapter);
        allFoodList = cnfDatabaseHandler.getAllItems();


        autoText = new AutoText(allFoodList, editText, this, autoTextListViewAdapter);
        editText.requestFocus();

    }
}
