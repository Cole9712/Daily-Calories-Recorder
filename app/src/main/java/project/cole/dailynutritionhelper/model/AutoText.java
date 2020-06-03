package project.cole.dailynutritionhelper.model;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;

import project.cole.dailynutritionhelper.R;
import project.cole.dailynutritionhelper.ui.AutoTextListViewAdapter;

public class AutoText implements PassedData {
    // Searched info
    private ArrayList<Item> myList = new ArrayList<>();
    // list in database
    private ArrayList<Item> list;

    private AutoTextListViewAdapter autoTextListViewAdapter;
    private ListView listView;
    private EditText editText;
    private Activity activity;
    private PopupWindow popupWindow;

    public AutoText(ArrayList<Item> list, EditText editText, Activity activity) {
        this.list = list;
        this.editText = editText;
        this.activity = activity;
        autoTextListViewAdapter = new AutoTextListViewAdapter(activity, this);
        setPopupWindow();
        setListener();
    }

    private void setListener() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (myList != null) myList.clear();
                if (s.length() > 0) {
                    for (Item item : list) {
                        if (item.getFoodName().contains(s)) {
                            myList.add(item);
                        }
                    }
                    autoTextListViewAdapter.setData(myList, String.valueOf(s));
                    popupWindow.showAsDropDown(editText);
                } else {
                    autoTextListViewAdapter.setData(null, null);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setPopupWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.activity_auto_text, null);

        listView = (ListView) linearLayout.findViewById(R.id.popupListView);
        listView.setAdapter(autoTextListViewAdapter);

        popupWindow = new PopupWindow();
        popupWindow.setWidth(editText.getWidth());
        popupWindow.setHeight(220);
        popupWindow.setContentView(linearLayout);
    }

    @Override
    public void passingData(String data) {
        editText.setText(data);
        popupWindow.dismiss();
    }


}
