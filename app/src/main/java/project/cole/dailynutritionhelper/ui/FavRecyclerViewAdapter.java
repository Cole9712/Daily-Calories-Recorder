package project.cole.dailynutritionhelper.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.List;

import project.cole.dailynutritionhelper.R;
import project.cole.dailynutritionhelper.data.CNFDatabaseHandler;
import project.cole.dailynutritionhelper.data.UserMealDatabaseHandler;
import project.cole.dailynutritionhelper.model.Item;

public class FavRecyclerViewAdapter extends RecyclerView.Adapter<FavRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Item> itemList;
    private LayoutInflater inflater;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;

    public FavRecyclerViewAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public void onBindViewHolder(@NonNull FavRecyclerViewAdapter.ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.foodName.setText(item.getFoodName());

        holder.quantityAndWeight.setText(MessageFormat.format(":{0}", item.getKcal()));

    }

    @NonNull
    @Override
    public FavRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView foodName;
        private TextView quantityAndWeight;
        private TextView dateAdded;
        private Button saveButton;
        private EditText foodItem;
        private EditText itemQuantity;
        private EditText itemWeight;
        private Button noButton;
        private Button yesButton;
        private ImageView Kcal;
        Button editButton;
        Button deleteButton;
        public int id;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            foodName = itemView.findViewById(R.id.foodName);
            quantityAndWeight = itemView.findViewById(R.id.quantity_or_weight);
            dateAdded = itemView.findViewById(R.id.date);
            Kcal = itemView.findViewById(R.id.Kcal);

            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            quantityAndWeight.setTextSize(18);
            editButton.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_addfolder));
            Kcal.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_calories));
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder = new AlertDialog.Builder(context);
                    inflater = LayoutInflater.from(context);
                    View confirmView = inflater.inflate(R.layout.confirm_for_delete, null);
                    noButton = confirmView.findViewById(R.id.noButton);
                    yesButton = confirmView.findViewById(R.id.yesButton);

                    builder.setView(confirmView);
                    dialog = builder.create();
                    dialog.show();
                    yesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            id = itemList.get(getAdapterPosition()).getFoodID();
                            CNFDatabaseHandler cnfDatabaseHandler = new CNFDatabaseHandler(context);
                            cnfDatabaseHandler.deleteFavItem(id);
                            itemList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());

                            dialog.dismiss();
                        }
                    });
                    noButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });


        }
    }


}
