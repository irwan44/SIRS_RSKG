package averin.sirs_rskg.com.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import averin.sirs_rskg.com.Model.isiSpinner;
import averin.sirs_rskg.com.R;

public class ListPoliAdapter extends RecyclerView.Adapter<ListPoliAdapter.ListPoliViewHolder> {
    private ArrayList<isiSpinner> list;
    private String idPoli,nmPoli;

    public ListPoliAdapter(ArrayList<isiSpinner> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ListPoliViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_poli, parent, false);
        return new ListPoliViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListPoliViewHolder holder, int position) {
        holder.txt_idPoli.setText(list.get(position).getId());
        holder.nama_poli.setText(list.get(position).getKet());
    }

    public class ListPoliViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_idPoli, txt_nmPoli;
        public RadioButton nama_poli;

        public ListPoliViewHolder(View itemView) {
            super(itemView);
//            txt_idPoli  = itemView.findViewById(R.id.id_poli);
            nama_poli  = itemView.findViewById(R.id.namapoli);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent i = new Intent(itemView.getContext(), AntrianDetail.class);
//                    String nmbagian_dtl = txt_nmPoli.getText().toString();
//                    itemView.getContext().startActivity(i);
                }
            });
        }
    }
}