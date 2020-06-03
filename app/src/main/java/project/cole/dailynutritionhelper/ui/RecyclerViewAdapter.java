package project.cole.dailynutritionhelper.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.lang3.math.NumberUtils;

import java.text.MessageFormat;
import java.util.List;

import project.cole.dailynutritionhelper.R;
import project.cole.dailynutritionhelper.data.UserMealDatabaseHandler;
import project.cole.dailynutritionhelper.model.Item;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Item> itemList;
    private AlertDialog confirmDialog;
    private AlertDialog.Builder confirmBuilder;
    private LayoutInflater inflater;


    public RecyclerViewAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);

        return new ViewHolder(view, context);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.foodName.setText(item.getFoodName());
        if (item.getFoodQuantity() == 0) {
            holder.quantityAndWeight.setText(MessageFormat.format("Weight: {0}g", item.getFoodWeightInGrams()));
        } else if (item.getFoodWeightInGrams() == 0) {
            holder.quantityAndWeight.setText(MessageFormat.format("Amount: x{0}", item.getFoodQuantity()));
        } else {
            holder.quantityAndWeight.setText(MessageFormat.format("Amount: x{0} Weight: {1}g", item.getFoodQuantity(), item.getFoodWeightInGrams()));
        }
        holder.dateAdded.setText(item.getDateItemAdded());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView foodName;
        private TextView quantityAndWeight;
        private TextView dateAdded;
        private Button saveButton;
        private EditText foodItem;
        private EditText itemQuantity;
        private EditText itemWeight;
        private Button noButton;
        private Button yesButton;
        Button editButton;
        Button deleteButton;
        public int id;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            foodName = itemView.findViewById(R.id.foodName);
            quantityAndWeight = itemView.findViewById(R.id.quantity_or_weight);
            dateAdded = itemView.findViewById(R.id.date);

            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.editButton:
                    final Item item = itemList.get(getAdapterPosition());
                    confirmBuilder = new AlertDialog.Builder(context);
                    inflater = LayoutInflater.from(context);
                    View view = inflater.inflate(R.layout.popup, null);
                    foodItem = view.findViewById(R.id.foodItem);
                    itemQuantity = view.findViewById(R.id.itemQuantity);
                    itemWeight = view.findViewById(R.id.itemWeight);
                    saveButton = view.findViewById(R.id.saveButton);

                    saveButton.setText(R.string.update);
                    foodItem.setText(item.getFoodName());
                    itemQuantity.setText(String.valueOf(item.getFoodQuantity()));
                    itemWeight.setText(String.valueOf(item.getFoodWeightInGrams()));

                    confirmBuilder.setView(view);
                    confirmDialog = confirmBuilder.create();
                    confirmDialog.show();
                    saveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (foodItem.getText().toString().isEmpty()) {
                                Snackbar.make(v, "Please Enter the Food Name", Snackbar.LENGTH_SHORT).show();
                            } else if (itemQuantity.getText().toString().isEmpty() && itemWeight.getText().toString().isEmpty()) {
                                Snackbar.make(v, "Please Enter Quantity/Weight", Snackbar.LENGTH_SHORT).show();
                            } else {
                                item.setId(item.getId());
                                item.setFoodName(foodItem.getText().toString().trim());
                                item.setFoodQuantity(NumberUtils.toInt(itemQuantity.getText().toString().trim()));
                                item.setFoodWeightInGrams(NumberUtils.toInt(itemWeight.getText().toString().trim()));

                                UserMealDatabaseHandler userMealDatabaseHandler = new UserMealDatabaseHandler(context);
                                userMealDatabaseHandler.updateItem(item);
                                notifyItemChanged(getAdapterPosition(), item);
                                Snackbar.make(v, "Food Information Saved", Snackbar.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        confirmDialog.dismiss();

                                    }
                                }, 1000);

                            }

                        }
                    });

                    break;
                case R.id.deleteButton:
                    confirmBuilder = new AlertDialog.Builder(context);
                    inflater = LayoutInflater.from(context);
                    View confirmView = inflater.inflate(R.layout.confirm_for_delete, null);
                    noButton = confirmView.findViewById(R.id.noButton);
                    yesButton = confirmView.findViewById(R.id.yesButton);

                    confirmBuilder.setView(confirmView);
                    confirmDialog = confirmBuilder.create();
                    confirmDialog.show();
                    yesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            id = itemList.get(getAdapterPosition()).getId();
                            UserMealDatabaseHandler db = new UserMealDatabaseHandler(context);
                            db.deleteItem(id);
                            itemList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());

                            confirmDialog.dismiss();
                        }
                    });
                    noButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmDialog.dismiss();
                        }
                    });
                    break;


            }
        }
    }

}
