package averin.sirs_rskg.com.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import averin.sirs_rskg.com.Model.isiSpinner;
import averin.sirs_rskg.com.R;
import averin.sirs_rskg.com.Ui.ItemClickListener;

public class PoliListAdapter extends RecyclerView.Adapter<PoliListAdapter.ViewHolder> {

    // Initialize variable
    ArrayList<isiSpinner> arrayList;
    ItemClickListener itemClickListener;
    int selectedPosition = -1;

    // Create constructor
    public PoliListAdapter(ArrayList<isiSpinner> arrayList, ItemClickListener itemClickListener)
    {
        this.arrayList = arrayList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // Initialize view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_poli, parent,
                        false);
        // Pass holder view
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        // Set text on radio button
        holder.radioButton.setText(arrayList.get(position).getKet());

        // Checked selected radio button
        holder.radioButton.setChecked(position == selectedPosition);

        // set listener on radio button
        holder.radioButton.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(
                            CompoundButton compoundButton,
                            boolean b)
                    {
                        // check condition
                        if (b) {
                            // When checked
                            // update selected position
                            selectedPosition
                                    = holder.getAdapterPosition();
                            // Call listener
                            itemClickListener.onClick(
                                    holder.radioButton.getText()
                                            .toString());
                        }
                    }
                });
    }

    @Override public long getItemId(int position)
    {
        // pass position
        return position;
    }
    @Override public int getItemViewType(int position)
    {
        // pass position
        return position;
    }

    @Override public int getItemCount()
    {
        // pass total list size
        return arrayList.size();
    }

    public class ViewHolder
            extends RecyclerView.ViewHolder {
        // Initialize variable
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            // Assign variable
            radioButton
                    = itemView.findViewById(R.id.namapoli);
        }
    }
}