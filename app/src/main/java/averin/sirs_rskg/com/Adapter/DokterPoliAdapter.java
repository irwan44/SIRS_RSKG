package averin.sirs_rskg.com.Adapter;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import averin.sirs_rskg.com.DaftarPoli;
import averin.sirs_rskg.com.Model.DokterPoli;
import averin.sirs_rskg.com.R;
import android.util.Base64;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DokterPoliAdapter extends RecyclerView.Adapter<DokterPoliAdapter.DokterPoliViewHolder> {
    private ArrayList<DokterPoli> list;
    public Dialog dialog_regist_poli;
    public Button btnBatal, btnKirim;
    private String strFoto,jam_konvert_awal,jam_konvert_akhir,jam_awal,jam_akhir, range_hari;
    private byte[] byt_Foto;
    DateFormat outputwaktu = new SimpleDateFormat("HH:mm", Locale.getDefault());
    DateFormat inputwaktu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DokterPoliAdapter(ArrayList<DokterPoli> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public DokterPoliViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_dokter_poli, parent, false);
        return new DokterPoliViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DokterPoliViewHolder holder, int position) {
        holder.txt_namaDokter.setText(list.get(position).getNama_dokter());
        holder.txt_JenisPoli.setText(list.get(position).getBagian());
        holder.img_Dokter.setImageResource(R.drawable.logo_dmedis);
        holder.txt_kddokter.setText(list.get(position).getKode_dokter());
        holder.txt_idnyadokter.setText(list.get(position).getIdnya_dokter());
        holder.txt_kdbag.setText(list.get(position).getKode_bag());
        holder.txt_kdklinik.setText(list.get(position).getKd_klinik());
        holder.txt_nmklinik.setText(list.get(position).getNm_klinik());
        holder.txt_wktperiksa.setText(list.get(position).getWaktu_periksa());
        range_hari = list.get(position).getRange_hari();
        jam_awal = list.get(position).getJam_mulai();
        jam_akhir = list.get(position).getJam_akhir();

        //KONVERT WAKTU DOKTER
        Date wkt_awal = null;
        Date wkt_akhir = null;
        try {
            wkt_awal = inputwaktu.parse(jam_awal);
            wkt_akhir = inputwaktu.parse(jam_akhir);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        jam_konvert_awal = outputwaktu.format(wkt_awal);
        jam_konvert_akhir = outputwaktu.format(wkt_akhir);
        holder.txt_jamPeriksa.setText(range_hari+" \n( "+ jam_konvert_awal+ " - "+jam_konvert_akhir+" )");

        //DECODE FOTO DOKTER
        holder.txt_sFoto.setText(list.get(position).getFoto_dokter());
        strFoto = holder.txt_sFoto.getText().toString();
        if (strFoto.equals("")) {
            holder.img_Dokter.setImageResource(R.drawable.doctor1);
        } else {
            byt_Foto = Base64.decode(strFoto, Base64.DEFAULT);
            Bitmap bitmapDokter = BitmapFactory.decodeByteArray(byt_Foto, 0, byt_Foto.length);
            holder.img_Dokter.setImageBitmap(bitmapDokter);
        }
    }

    public class DokterPoliViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_kdbag, txt_kdklinik, txt_kddokter, txt_idnyadokter, txt_namaDokter,
                txt_JenisPoli, txt_nmklinik, txt_wktperiksa,
                txt_jamPeriksa, txt_sFoto;
        public ImageView img_Dokter;

        public DokterPoliViewHolder(View itemView) {
            super(itemView);
            txt_namaDokter = (TextView) itemView.findViewById(R.id.namaDokter);
            txt_JenisPoli = (TextView) itemView.findViewById(R.id.txtJenisPoli);
            txt_kddokter = (TextView) itemView.findViewById(R.id.kd_dokter);
            txt_idnyadokter = (TextView) itemView.findViewById(R.id.idnya_dokter);
            txt_kdbag = (TextView) itemView.findViewById(R.id.kd_bag);
            txt_kdklinik = (TextView) itemView.findViewById(R.id.kd_klinik);
            txt_nmklinik = (TextView) itemView.findViewById(R.id.nm_klinik);
            txt_wktperiksa = (TextView) itemView.findViewById(R.id.wkt_periksa);
            txt_jamPeriksa = (TextView) itemView.findViewById(R.id.jamPeriksa);
            txt_sFoto = (TextView) itemView.findViewById(R.id.sFoto);

            img_Dokter = (ImageView) itemView.findViewById(R.id.img_Dokter);
            dialog_regist_poli = new Dialog(itemView.getContext());
            dialog_regist_poli.setContentView(R.layout.dialog_daftar_poli);
//            btnBatal = dialog_regist_poli.findViewById(R.id.btn_batal);
            btnKirim = dialog_regist_poli.findViewById(R.id.btn_kirim);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent i = new Intent(itemView.getContext(), DaftarPoli.class);
                    String urlFoto = txt_sFoto.getText().toString();
                    String kode_dokter = txt_kddokter.getText().toString();
                    String idnya_dokter = txt_idnyadokter.getText().toString();
                    String nama_pegawai = txt_namaDokter.getText().toString();
                    String kode_bagian = txt_kdbag.getText().toString();
                    String nama_bagian = txt_JenisPoli.getText().toString();
                    String kde_klinik = txt_kdklinik.getText().toString();
                    String nma_klinik = txt_nmklinik.getText().toString();
                    String durasi = txt_wktperiksa.getText().toString();
                    i.putExtra("urlFoto", urlFoto);
                    i.putExtra("kd_dokter", kode_dokter);
                    i.putExtra("idnya_dokter", idnya_dokter);
                    i.putExtra("nm_dokter", nama_pegawai);
                    i.putExtra("kd_bag", kode_bagian);
                    i.putExtra("nm_bag", nama_bagian);
                    i.putExtra("kd_klinik", kde_klinik);
                    i.putExtra("nm_klinik", nma_klinik);
                    i.putExtra("durasi", durasi);
                    itemView.getContext().startActivity(i);

                }
            });
        }
    }
}


