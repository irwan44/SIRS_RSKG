package averin.sirs_rskg.com.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import averin.sirs_rskg.com.DetailMR;
import averin.sirs_rskg.com.Model.Rad;
import averin.sirs_rskg.com.R;
import averin.sirs_rskg.com.ViewFotoRad;
import averin.sirs_rskg.com.WebViewPDF;

public class RadAdapter extends RecyclerView.Adapter<RadAdapter.RadViewHolder> {

    private final ClickListener listener;
    private ArrayList<Rad> list, lst;
    private byte[] bytIMGKlinik;
    private String urlimg;
    public Context context;

    public RadAdapter(DetailMR ma, ArrayList<Rad> list, Context context, ClickListener listener) {
        this.listener = listener;
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
    public RadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View view = layoutInflater.inflate(R.layout.list_item_hasil_lab, parent, false), listener;
//        return new RadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_hasil_lab), parent, false), listener);

        return new RadViewHolder(layoutInflater.inflate(R.layout.list_item_hasil_rad, parent, false), listener);

    }

    @Override
    public void onBindViewHolder(RadViewHolder holder, int position) {
        holder.txt_url_rs.setText(list.get(position).getUrl_pdf_rad());
        holder.txt_nourut.setText(list.get(position).getNo_urut());
        urlimg = String.valueOf(list.get(position).getUrl_rs());
        holder.txt_kodeklinik.setText(list.get(position).getKode_klinik());
        holder.txt_kode_penunjang.setText(list.get(position).getKode_penunjang());
        holder.txt_kode_tchasilpenunjang.setText(list.get(position).getTc_hasilpenunjang());
        holder.txt_kategori.setText(list.get(position).getTxt_kategori());
        holder.txt_kode_tarif.setText(list.get(position).getKode_tarif());
        holder.txt_nama_tindakan.setText(list.get(position).getTindakanrad());
        holder.txt_jmlThorax.setText(list.get(position).getJmlThorax());
        String isi_url = holder.txt_url_rs.getText().toString();

        if(isi_url.equals("500")) {
            holder.btn_CetakHasil.setVisibility(View.GONE);
            holder.txt_null_hasil_rad.setVisibility(View.VISIBLE);
        }else{
            holder.btn_CetakHasil.setVisibility(View.VISIBLE);
            holder.txt_null_hasil_rad.setVisibility(View.GONE);
        }
    }

    public interface ClickListener {

        void onPositionClicked(int position);
        void onLongClicked(int position);
    }

    public class RadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_url_rs, txt_kodeklinik, txt_kode_tchasilpenunjang, txt_kode_penunjang, txt_kategori, txt_jmlThorax, txt_kode_tarif,
                txt_nama_tindakan, txt_null_hasil_rad, txt_nourut;
        public Button btn_CetakHasil, btn_Viewfoto;
        private WeakReference<ClickListener> listenerRef;

        public RadViewHolder(final View itemView, ClickListener listener) {
            super(itemView);

            listenerRef                 = new WeakReference<>(listener);
            txt_nourut                  = itemView.findViewById(R.id.no_urut);
            txt_url_rs                  = itemView.findViewById(R.id.url_rs);
            txt_kodeklinik              = itemView.findViewById(R.id.kd_klinik);
            txt_kode_penunjang          = itemView.findViewById(R.id.kode_penunjang);
            txt_kode_tchasilpenunjang   = itemView.findViewById(R.id.kode_tc_hasilpenunjang);
            txt_kode_tarif              = itemView.findViewById(R.id.kode_tarif);
            txt_kategori                = itemView.findViewById(R.id.txt_kategori);
            txt_nama_tindakan           = itemView.findViewById(R.id.txt_namatindakan);
            txt_jmlThorax               = itemView.findViewById(R.id.jmlThorax);
            txt_null_hasil_rad          = itemView.findViewById(R.id.txt_null_hasil_rad);
            btn_CetakHasil              = itemView.findViewById(R.id.btn_cetakhasil);
            btn_Viewfoto                = itemView.findViewById(R.id.btn_lihatfoto);
//            img_fotohasil             = itemView.findViewById(R.id.img_fotoKlinik);

            itemView.setOnClickListener(this);
            btn_CetakHasil.setOnClickListener(this);
            btn_Viewfoto.setOnClickListener(this);

        }

        // onClick Listener for view
        @Override
        public void onClick(View v) {

            if (v.getId() == btn_CetakHasil.getId()) {

                Intent i =  new Intent(itemView.getContext(),WebViewPDF.class);
                String url_rs                   = txt_url_rs.getText().toString();
                String kd_tchasilpenunjang = txt_kode_tchasilpenunjang.getText().toString();
                String namatindakan = txt_nama_tindakan.getText().toString();
                i.putExtra("kategori","3");
                i.putExtra("url_pdf", url_rs);
                i.putExtra("title_bar", "Hasil Radiologi");
                i.putExtra("tindakan", "Pemeriksaan "+namatindakan);
                i.putExtra("namafile", "hasilrad_"+kd_tchasilpenunjang);
                itemView.getContext().startActivity(i);

//                Toast.makeText(v.getContext(), "ITEM PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            } else if(v.getId() == btn_Viewfoto.getId()){
                Intent i = new Intent(itemView.getContext(), ViewFotoRad.class);
                String kd_klinik    = txt_kodeklinik.getText().toString();
                String kd_penunjang = txt_kode_penunjang.getText().toString();
                String kd_tarif     = txt_kode_tarif.getText().toString();
                String namatindakan = txt_nama_tindakan.getText().toString();
                i.putExtra("url_rs", urlimg);
                i.putExtra("kode_klinik", kd_klinik);
                i.putExtra("kode_penunjang", kd_penunjang);
                i.putExtra("kode_tarif", kd_tarif);
                i.putExtra("nama_tindakan", namatindakan);
                itemView.getContext().startActivity(i);

            }
            listenerRef.get().onPositionClicked(getAdapterPosition());
        }
    }
}