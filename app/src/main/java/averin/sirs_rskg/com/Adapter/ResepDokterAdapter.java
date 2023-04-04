package averin.sirs_rskg.com.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import averin.sirs_rskg.com.Model.ResepObat;
import averin.sirs_rskg.com.R;

public class ResepDokterAdapter extends RecyclerView.Adapter<ResepDokterAdapter.MyView> {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ResepObat> list;

    public class MyView extends RecyclerView.ViewHolder {
        private TextView nourut, nama_obat, jumlah_obat, note_obat, aturan_pakai, ket_obat, jenisobat, jns_obat;

        public MyView(View view) {
            super(view);

            nourut = (TextView) itemView.findViewById(R.id.nourut);
            nama_obat = (TextView) itemView.findViewById(R.id.nmobat);
            jumlah_obat = (TextView) itemView.findViewById(R.id.jmlobat);
            note_obat = (TextView) itemView.findViewById(R.id.noteobat);
            aturan_pakai = (TextView) itemView.findViewById(R.id.aturanpakai);
            ket_obat =  (TextView) itemView.findViewById(R.id.ketobat);
            jenisobat = (TextView) itemView.findViewById(R.id.jenisobat);
            jns_obat = (TextView) itemView.findViewById(R.id.jnsobat);
        }
    }

    public ResepDokterAdapter(List<ResepObat> horizontalList)
    {
        this.list = horizontalList;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_resep_dokter, parent, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {
        ResepObat data;
        data = list.get(position);
        holder.nourut.setText(data.getNourut());
        holder.nama_obat.setText(data.getNamaobat());
        holder.jumlah_obat.setText(data.getJumlahobat());
        holder.note_obat.setText(data.getNoteobat());
        holder.aturan_pakai.setText((data.getNamadosis()));
        holder.ket_obat.setText(data.getKetobat());
        holder.jenisobat.setText(data.getJnsobat());
        holder.jns_obat.setText(data.getJnsobat());
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }
}
