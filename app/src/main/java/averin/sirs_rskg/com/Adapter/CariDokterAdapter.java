package averin.sirs_rskg.com.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import averin.sirs_rskg.com.Model.DokterPoli;
import averin.sirs_rskg.com.R;

public class CariDokterAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DokterPoli> list;

    public TextView txt_kdbag, txt_kdklinik, txt_kddokter, txt_idnyadokter, txt_namaDokter,
            txt_JenisPoli, txt_nmklinik, txt_wktperiksa,
            txt_jamPeriksa, txt_sFoto;
    public ImageView img_Dokter;

    private String strFoto,jam_konvert_awal,jam_konvert_akhir,jam_awal,jam_akhir, range_hari;;
    private byte[] byt_Foto;
    DateFormat outputwaktu = new SimpleDateFormat("HH:mm", Locale.getDefault());
    DateFormat inputwaktu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public CariDokterAdapter(Activity activity, List<DokterPoli> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int location) {
        return list.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View itemView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (itemView == null)
            itemView = inflater.inflate(R.layout.list_item_dokter_poli, null);

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

        txt_namaDokter.setText(list.get(position).getNama_dokter());
        txt_JenisPoli.setText(list.get(position).getBagian());
        img_Dokter.setImageResource(R.drawable.logo_rskg);
        txt_kddokter.setText(list.get(position).getKode_dokter());
        txt_idnyadokter.setText(list.get(position).getIdnya_dokter());
        txt_kdbag.setText(list.get(position).getKode_bag());
        txt_kdklinik.setText(list.get(position).getKd_klinik());
        txt_nmklinik.setText(list.get(position).getNm_klinik());
        txt_wktperiksa.setText(list.get(position).getWaktu_periksa());

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
        txt_jamPeriksa.setText(range_hari+" \n( "+ jam_konvert_awal+ " - "+jam_konvert_akhir+" )");

        //DECODE FOTO DOKTER
        txt_sFoto.setText(list.get(position).getFoto_dokter());
        strFoto = txt_sFoto.getText().toString();
        if (strFoto.equals("")) {
            img_Dokter.setImageResource(R.drawable.profile22);
        } else {
//            Glide.with(activity).load(strFoto).into(img_Dokter);
            byt_Foto = Base64.decode(strFoto, Base64.DEFAULT);
            Bitmap bitmapDokter = BitmapFactory.decodeByteArray(byt_Foto, 0, byt_Foto.length);
            img_Dokter.setImageBitmap(bitmapDokter);
        }

        return itemView;
    }
}