package averin.sirs_rskg.com.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import averin.sirs_rskg.com.DetailMR;
import averin.sirs_rskg.com.Model.Lab;
import averin.sirs_rskg.com.R;
import averin.sirs_rskg.com.WebViewPDF;

public class LabAdapter extends RecyclerView.Adapter<LabAdapter.LabViewHolder> {
    private ArrayList<Lab> list, lst;
    private byte[] bytIMGKlinik;
    private String url_file_pdf;
    public Context context;

    public LabAdapter(DetailMR ma, ArrayList<Lab> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

//    public int getItemCount() {
//        return (list == null) ? 0 : list.size();
//    }

    @Override
    public LabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_hasil_lab, parent, false);
        return new LabViewHolder(view);

    }

    @Override
    public void onBindViewHolder(LabViewHolder holder, int position) {
        holder.txt_url_rs.setText(list.get(position).getUrl_rs());
        holder.txt_kode_penunjang.setText(list.get(position).getKode_penunjang());
        holder.txt_nourut.setText(list.get(position).getNo_urut());
        holder.txt_kategori.setText(list.get(position).getTxt_kategori());
        holder.txt_nomr_px.setText(list.get(position).getNomr_px());
        holder.txt_nama_tindakan.setText(list.get(position).getNama_tindakan());
        holder.txt_tglhasil.setText(list.get(position).getTglhasil());

        url_file_pdf = holder.txt_url_rs.getText().toString();
    }

    public class LabViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_url_rs, txt_kode_penunjang, txt_kategori, txt_nomr_px, txt_nama_tindakan, txt_tglhasil, txt_nourut;

        public LabViewHolder(View itemView) {

            super(itemView);
            txt_url_rs              = itemView.findViewById(R.id.url_rs);
            txt_kode_penunjang      = itemView.findViewById(R.id.kode_penunjang);
            txt_nourut             = itemView.findViewById(R.id.no_urut);
            txt_nomr_px             = itemView.findViewById(R.id.nomr_px);
            txt_kategori            = itemView.findViewById(R.id.txt_kategori);
            txt_nama_tindakan       = itemView.findViewById(R.id.txt_namatindakan);
            txt_tglhasil            = itemView.findViewById(R.id.txt_tglhasil);
//            img_fotohasil             = itemView.findViewById(R.id.img_fotoKlinik);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(url_file_pdf.equals("500")){
                        Toast.makeText(itemView.getContext(),"File tidak tersedia", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent i = new Intent(itemView.getContext(), WebViewPDF.class);
                        String namatindakan = txt_nama_tindakan.getText().toString();
                        String kd_penunjang = txt_kode_penunjang.getText().toString();
                        i.putExtra("kategori","2");
                        i.putExtra("url_pdf", url_file_pdf);
                        i.putExtra("title_bar", "Hasil Laboratorium");
                        i.putExtra("tindakan", "Pemeriksaan "+ namatindakan);
                        i.putExtra("namafile", "hasillab_"+kd_penunjang);
                        itemView.getContext().startActivity(i);
                    }
                }
            });
        }
    }
}