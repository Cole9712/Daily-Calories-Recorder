package project.cole.dailynutritionhelper.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;

import project.cole.dailynutritionhelper.AutoTextActivity;
import project.cole.dailynutritionhelper.FavListActivity;
import project.cole.dailynutritionhelper.R;
import project.cole.dailynutritionhelper.data.CNFDatabaseHandler;
import project.cole.dailynutritionhelper.model.AutoText;
import project.cole.dailynutritionhelper.model.Item;
import project.cole.dailynutritionhelper.model.PassedData;

public class AutoTextListViewAdapter extends BaseAdapter {
    private ArrayList<Item> list = new ArrayList<>();
    private Activity activity;
    private String keyWord;
    private PassedData passedData;
    CNFDatabaseHandler cnfDatabaseHandler;

    public AutoTextListViewAdapter(Activity activity, PassedData passedData) {
        this.activity = activity;
        this.passedData = passedData;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Item item = list.get(position);
        TextView textView = new TextView(activity);
        textView.setTextSize(18);
        textView.setId(position);
        textView.setText(MessageFormat.format("{0}", item.getFoodName()));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CNFDatabaseHandler cnfDatabaseHandler = new CNFDatabaseHandler(activity);
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                LayoutInflater layoutInflater = LayoutInflater.from(activity);
                View alertView = layoutInflater.inflate(R.layout.confirm_for_delete, null);
                Button noButton = alertView.findViewById(R.id.noButton);
                Button yesButton = alertView.findViewById(R.id.yesButton);
                TextView alertText = alertView.findViewById(R.id.alertText);
                TextView foodInfoText = alertView.findViewById(R.id.foodInfo);
                foodInfoText.setPadding(50, 20, 50, 20);

                item.setKcal(Integer.parseInt(cnfDatabaseHandler.getKCal(item.getFoodID())));
                alertText.setText(MessageFormat.format("{0}\n{1}", "Add this item to Favourite?", item.getFoodName()));
                foodInfoText.setText(MessageFormat.format("Energy: {0} kCal/100g\nProtein: {1} g/100g\nFat: {2} g/100g\nCarbohydrate: {3} g/100g", item.getKcal(), cnfDatabaseHandler.getProtein(item.getFoodID()), cnfDatabaseHandler.getFat(item.getFoodID()), cnfDatabaseHandler.getCarb(item.getFoodID())));
                builder.setView(alertView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cnfDatabaseHandler.addFavItem(item);
                        alertDialog.dismiss();
                        activity.finish();

                    }
                });
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });


//                passedData.passingData(list.get(v.getId()).getFoodName());
            }
        });

        return textView;
    }

    public void setData(ArrayList<Item> list, String data) {
        this.list = list;
        this.keyWord = data;
        if (list != null) {
            for (Item item : list) {
                Log.d("Adapter", "setData: " + item.getFoodName());
            }
        } else {
            Log.d("Adapter", "NULL");
        }

        this.notifyDataSetChanged();
    }
}
