package averin.sirs_rskg.com.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import averin.sirs_rskg.com.Model.isiSpinner;
import averin.sirs_rskg.com.R;

public class DetailMRAdapter extends RecyclerView.Adapter<DetailMRAdapter.DetailMRViewHolder> {
    public Context context;
    private Activity activity;
    private ArrayList<isiSpinner> list;

    public DetailMRAdapter(Activity activity, ArrayList<isiSpinner> list, Context context) {
        this.activity = activity;
        this.list = list;
        this.context = context;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public DetailMRAdapter.DetailMRViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.dialog_spinner, parent, false);
        return new DetailMRAdapter.DetailMRViewHolder(view);

    }

    @Override
    public void onBindViewHolder(DetailMRAdapter.DetailMRViewHolder holder, int position) {
        holder.txt_ket.setText(list.get(position).getId() + ". " + list.get(position).getKet());
    }

    public class DetailMRViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_ket;

        public DetailMRViewHolder(View itemView) {
            super(itemView);
            txt_ket = itemView.findViewById(R.id.txt_ket);

        }
    }
}
