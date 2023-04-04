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

import averin.sirs_rskg.com.AntrianDetail;
import averin.sirs_rskg.com.MainActivity;
import averin.sirs_rskg.com.Model.Antrian;
import averin.sirs_rskg.com.R;

public class AntrianDSBAdapter extends RecyclerView.Adapter<AntrianDSBAdapter.AntrianDSBViewHolder> {
    private ArrayList<Antrian> list;
    private String tgl_awal,tgl_konvert,jam_awal,jam_konvert;
    DateFormat outputwaktu = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat inputwaktu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DateFormat outputjam = new SimpleDateFormat("HH:mm");
    DateFormat inputjam = new SimpleDateFormat("HH:mm:ss");

    public AntrianDSBAdapter(MainActivity ma, ArrayList<Antrian> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public AntrianDSBViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_antrian_homepage, parent, false);
        return new AntrianDSBViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AntrianDSBViewHolder holder, int position) {
        holder.txt_idReg.setText(list.get(position).getIdReg_antrian());
        holder.txt_noantrian.setText(list.get(position).getNo_antrian());
        holder.txt_nmDokter.setText(list.get(position).getnmDokter_antrian());
//        holder.txt_jamAwal.setText(list.get(position).getJamAwal_antrian());
        holder.txt_jamAkhir.setText(list.get(position).getJamAkhir_antrian());
        holder.txt_status.setText(list.get(position).getStatus_antrian());
        holder.txt_nmKlinik.setText(list.get(position).getnmKlinik_antrian());
        holder.txt_nmPoli.setText(list.get(position).getnmBagian_antrian());

        //Konvert Tgl Antri
        tgl_awal = list.get(position).getTgl_antrian();
        Date wkt_awal = null;
        try {
            wkt_awal = inputwaktu.parse(tgl_awal);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        tgl_konvert = outputwaktu.format(wkt_awal);
        holder.txt_tglAntrian.setText(tgl_konvert+", ");

        //Konvert Jam Awal
        jam_awal = list.get(position).getJamAwal_antrian();
        Date awal_jam = null;
        try {
            awal_jam = inputjam.parse(jam_awal);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        jam_konvert = outputjam.format(awal_jam);
        holder.txt_jamAwal.setText(jam_konvert);
    }

    public class AntrianDSBViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_idReg, txt_jamAkhir, txt_noantrian, txt_nmKlinik, txt_nmDokter,
                txt_nmPoli, txt_tglAntrian, txt_jamAwal, txt_status;

        public AntrianDSBViewHolder(View itemView) {
            super(itemView);
            txt_noantrian     = itemView.findViewById(R.id.txt_noantrian);
            txt_nmKlinik      = itemView.findViewById(R.id.txt_nmKlinik);
            txt_nmDokter      = itemView.findViewById(R.id.txt_nmDokter);
            txt_nmPoli        = itemView.findViewById(R.id.txt_nmPoli);
            txt_tglAntrian    = itemView.findViewById(R.id.txt_tglAntrian);
            txt_jamAwal       = itemView.findViewById(R.id.txt_jamAwal);
            txt_jamAkhir      = itemView.findViewById(R.id.txt_jamAkhir);
            txt_idReg         = itemView.findViewById(R.id.txt_idReg);
            txt_status         = itemView.findViewById(R.id.txt_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(itemView.getContext(), AntrianDetail.class);
//                    Declarate String
                    String regId        = txt_idReg.getText().toString();
                    String no_antri     = txt_noantrian.getText().toString();
                    String nmaDokter    = txt_nmDokter.getText().toString();
                    String tgl_dtl      = txt_tglAntrian.getText().toString();
                    String jawal_dtl    = txt_jamAwal.getText().toString();
                    String jakhir_dtl   = txt_jamAkhir.getText().toString();
                    String status_dtl   = txt_status.getText().toString();
                    String nmKlinik_dtl = txt_nmKlinik.getText().toString();
                    String nmbagian_dtl = txt_nmPoli.getText().toString();
//                    Put to parsing
                    i.putExtra("regId", regId);
                    i.putExtra("noAntrian", no_antri);
                    i.putExtra("nma_dokter", nmaDokter);
                    i.putExtra("tgl_antri", tgl_dtl);
                    i.putExtra("jam_awal", jawal_dtl);
                    i.putExtra("jam_akhir", jakhir_dtl);
                    i.putExtra("status_antri", status_dtl);
                    i.putExtra("nm_klinik", nmKlinik_dtl);
                    i.putExtra("nm_bag", nmbagian_dtl);
                    itemView.getContext().startActivity(i);
                }
            });
        }
    }
}