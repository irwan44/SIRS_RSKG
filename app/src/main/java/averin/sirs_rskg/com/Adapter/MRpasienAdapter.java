package averin.sirs_rskg.com.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import averin.sirs_rskg.com.DetailMR;
import averin.sirs_rskg.com.Model.MRpasien;
import averin.sirs_rskg.com.R;

public class MRpasienAdapter extends RecyclerView.Adapter<MRpasienAdapter.MRpasienViewHolder> {
    private ArrayList<MRpasien> list;
    DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat outputwaktu = new SimpleDateFormat("HH:mm", Locale.getDefault());
    DateFormat inputwaktu = new SimpleDateFormat("HH:mm:ss");

    public MRpasienAdapter(ArrayList<MRpasien> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public MRpasienViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_mr, parent, false);
        return new MRpasienViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MRpasienViewHolder holder, int position) {
        Date tgl = null;
        Date wkt = null;
        String tgl_daft, wkt_daft;

        holder.tv_kodeklinik.setText(list.get(position).getKode_klinik());
        holder.tv_idreg.setText((list.get(position).getId_regist()));
        holder.tv_namadokter.setText(list.get(position).getNama_dokter());
        holder.tv_namabagian.setText(list.get(position).getNama_bagian());
        holder.tv_tgldaftar.setText(list.get(position).getTgl_periksa());
        holder.tv_wktdaftar.setText(list.get(position).getJam_periksa());
        holder.tv_namaklinik.setText(list.get(position).getNama_klinik());
        holder.txt_urlrs.setText(list.get(position).getUrl_rs());

        tgl_daft = holder.tv_tgldaftar.getText().toString();
        wkt_daft = holder.tv_wktdaftar.getText().toString();

        if(tgl_daft.equals("") && wkt_daft.equals("")) {
        }else {
            if (tgl_daft.equals("2")) {
                holder.txt_tglPeriksa.setText("Belum VItal Sign");
            } else {
                try {
                    tgl = inputFormat.parse(tgl_daft);
                    //                wkt = inputwaktu.parse(wkt_daft);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String tglKonvert = outputFormat.format(tgl);
//              String jam_konvert = outputwaktu.format(wkt);
                holder.txt_tglPeriksa.setText(tglKonvert + " " + wkt_daft);
            }
        }
//        holder.txt_namaKlinik.setText(list.get(position).getNama_klinik());
    }

    public class MRpasienViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_kodeklinik, tv_namaklinik, tv_idreg, tv_namadokter, tv_namabagian, tv_tgldaftar,
                tv_wktdaftar, txt_tglPeriksa, txt_urlrs;

        public MRpasienViewHolder(View itemView) {
            super(itemView);
            tv_idreg        = (TextView) itemView.findViewById(R.id.txt_idReg);
            tv_kodeklinik   = (TextView) itemView.findViewById(R.id.txt_kodeklinik);
            tv_namaklinik   = (TextView) itemView.findViewById(R.id.txt_namaklinik);
            tv_namadokter   = (TextView) itemView.findViewById(R.id.txt_namaDokter);
            tv_namabagian   = (TextView) itemView.findViewById(R.id.txt_namabagian);
            tv_tgldaftar    = (TextView) itemView.findViewById(R.id.txt_tgl_daftar);
            tv_wktdaftar    = (TextView) itemView.findViewById(R.id.txt_wkt_daftar);
            txt_tglPeriksa  = (TextView) itemView.findViewById(R.id.txt_tglPeriksa);
            txt_urlrs       = (TextView) itemView.findViewById(R.id.txt_urlrs);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(itemView.getContext(), DetailMR.class);

                    String idreg        = tv_idreg.getText().toString();
                    String kde_klinik   = tv_kodeklinik.getText().toString();
                    String nama_klinik  = tv_namaklinik.getText().toString();
                    String nama_dokter  = tv_namadokter.getText().toString();
                    String nama_bagian  = tv_namabagian.getText().toString();
                    String tgl_daftar   = txt_tglPeriksa.getText().toString();
                    String url_rs       = txt_urlrs.getText().toString();

                    i.putExtra("idRegKlinik", idreg);
                    i.putExtra("kd_klinik", kde_klinik);
                    i.putExtra("nama_klinik", nama_klinik);
                    i.putExtra("nama_bagian", nama_bagian);
                    i.putExtra("nama_dokter", nama_dokter);
                    i.putExtra("tgl_daftar", tgl_daftar);
                    i.putExtra("url_rs", url_rs);
                    itemView.getContext().startActivity(i);
                }
            });
        }
    }
}