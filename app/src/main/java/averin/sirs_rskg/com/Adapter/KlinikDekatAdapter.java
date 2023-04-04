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

public class KlinikDekatAdapter extends RecyclerView.Adapter<KlinikDekatAdapter.KlinikViewHolder> {
    private ArrayList<Klinik> list, lst;
    private byte[] bytIMGKlinik;
    private String urlGIF;
    public Context context;

    public KlinikDekatAdapter(MainActivity ma, ArrayList<Klinik> list, Context context) {
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
    public KlinikViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_klinik_terdekat, parent, false);
        return new KlinikViewHolder(view);

    }

    @Override
    public void onBindViewHolder(KlinikViewHolder holder, int position) {
        holder.idklinik.setText(list.get(position).getId_klinik());
        holder.txt_nmKlinik.setText(list.get(position).getNama_klinik());
        holder.urlFotoklinik.setText(list.get(position).getLogo_klinik());
        urlGIF = holder.urlFotoklinik.getText().toString();
        if (urlGIF.equals("null") || urlGIF.equals("")) {
            holder.img_fotoKlinik.setImageResource(R.drawable.sirs);
        } else {
            Glide.with(context).load(urlGIF).into(holder.img_fotoKlinik);
//            bytIMGKlinik = urlGIF.getBytes(StandardCharsets.UTF_8);
//            Bitmap bitmapDokter = BitmapFactory.decodeByteArray(bytIMGKlinik, 0, bytIMGKlinik.length);
//            holder.img_fotoKlinik.setImage
        }
//        Glide.with(context).load(urlGIF).into(holder.img_fotoKlinik);
    }

    public class KlinikViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_nmKlinik,txt_alamat, idklinik, urlFotoklinik;
        public ImageView img_fotoKlinik;

        public KlinikViewHolder(View itemView) {

            super(itemView);
            txt_nmKlinik    = itemView.findViewById(R.id.txt_nmKlinik);
//            txt_alamat      = itemView.findViewById(R.id.txt_alamat);
            idklinik        = itemView.findViewById(R.id.idklinik);
            urlFotoklinik   = itemView.findViewById(R.id.urlFotoklinik);
            img_fotoKlinik  = itemView.findViewById(R.id.img_fotoKlinik);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(itemView.getContext(), DetailKlinik.class);
                    String ini_id = idklinik.getText().toString();
                    i.putExtra("idk", ini_id);
                    itemView.getContext().startActivity(i);
                }
            });
        }
    }
}