package project.cole.dailynutritionhelper.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;

import project.cole.dailynutritionhelper.model.Item;
import project.cole.dailynutritionhelper.model.PassedData;

public class AutoTextListViewAdapter extends BaseAdapter {
    private ArrayList<Item> list = new ArrayList<>();
    private Activity activity;
    private String keyWord;
    private PassedData passedData;

    public AutoTextListViewAdapter(Activity activity, PassedData passedData) {
        this.activity = activity;
        this.passedData = passedData;
    }

    @Override
    public int getCount() {
        return list.size();
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
        TextView textView = new TextView(activity);
        textView.setTextSize(18);
        textView.setId(position);
        textView.setText(MessageFormat.format("{0}", list.get(position).getFoodName()));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passedData.passingData(list.get(v.getId()).getFoodName());
            }
        });

        return textView;
    }

    public void setData(ArrayList<Item> list, String data) {
        this.list = list;
        this.keyWord = data;
        this.notifyDataSetChanged();
    }
}
