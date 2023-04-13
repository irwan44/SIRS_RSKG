package averin.sirs_rskg.com.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import averin.sirs_rskg.com.DetailKlinik;
import averin.sirs_rskg.com.MainActivity;
import averin.sirs_rskg.com.Model.Klinik;
import averin.sirs_rskg.com.R;

public class KlinikshortAdapter extends RecyclerView.Adapter<KlinikshortAdapter.KlinikshortViewHolder> {
    private ArrayList<Klinik> list;
    public Context context;

    public KlinikshortAdapter(MainActivity ma, ArrayList<Klinik> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
//    public int getItemCount() {
//        return list.size();
//    }

    public int getItemCount() {
        return (list == null) ? 0 : list.size();
    }

    @Override
    public KlinikshortAdapter.KlinikshortViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_klinik, parent, false);
        return new KlinikshortAdapter.KlinikshortViewHolder(view);

    }

    @Override
    public void onBindViewHolder(KlinikshortAdapter.KlinikshortViewHolder holder, int position) {
        holder.namaKlinik.setText(list.get(position).getNama_klinik());
        holder.txt_alamat.setText(list.get(position).getAlamat_klinik());
        holder.idk.setText(list.get(position).getId_klinik());
        holder.urlLogo.setText(list.get(position).getLogo_klinik());
        String urlGIF = holder.urlLogo.getText().toString();
        if (urlGIF.equals("null") || urlGIF.equals("")) {
            holder.img_Klinik.setImageResource(R.drawable.logo_rskg);
        } else {
            Glide.with(context).load(urlGIF).into(holder.img_Klinik);
        }
    }

    public class KlinikshortViewHolder extends RecyclerView.ViewHolder {
        public TextView namaKlinik, txt_email, txt_map, txt_alamat, txt_tlp, txt_ket, idk, urlLogo;
        public ImageView img_Klinik;

        public KlinikshortViewHolder(View itemView) {
            super(itemView);
            namaKlinik = itemView.findViewById(R.id.namaKlinik);
            txt_alamat = itemView.findViewById(R.id.txt_alamat);
            urlLogo    = itemView.findViewById(R.id.urlFoto);
            img_Klinik = itemView.findViewById(R.id.img_iconKlinik);
            idk = itemView.findViewById(R.id.idk);

            Glide.with(itemView).load(R.drawable.logo_rskg).into(img_Klinik);

//            txt_email   = itemView.findViewById(R.id.txt_email);
//            txt_tlp = itemView.findViewById(R.id.txt_tlp);
//            txt_ket = itemView.findViewById(R.id.txt_ket);
//            txt_map = itemView.findViewById(R.id.txt_map);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(itemView.getContext(), DetailKlinik.class);
                    String ini_id = idk.getText().toString();
                    i.putExtra("idk", ini_id);
                    itemView.getContext().startActivity(i);
                }
            });
        }
    }
}


