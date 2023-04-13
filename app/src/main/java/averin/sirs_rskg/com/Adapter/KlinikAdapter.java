package averin.sirs_rskg.com.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import averin.sirs_rskg.com.DetailKlinik;
import averin.sirs_rskg.com.Model.Klinik;
import averin.sirs_rskg.com.R;

public class KlinikAdapter extends RecyclerView.Adapter<KlinikAdapter.KlinikViewHolder> {
    private ArrayList<Klinik> list, lst;
    private String urlGIF;
    public Context context;

    public KlinikAdapter(Activity activity, ArrayList<Klinik> list, Context context) {
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
        View view = layoutInflater.inflate(R.layout.list_item_klinik, parent, false);
        return new KlinikViewHolder(view);

    }

    @Override
    public void onBindViewHolder(KlinikViewHolder holder, int position) {
        holder.namaKlinik.setText(list.get(position).getNama_klinik());
        holder.txt_alamat.setText(list.get(position).getAlamat_klinik());
        holder.idk.setText(list.get(position).getId_klinik());
        holder.urlLogo.setText(list.get(position).getLogo_klinik());
        urlGIF = holder.urlLogo.getText().toString();
        if (urlGIF.equals("null") || urlGIF.equals("")) {
            holder.img_Klinik.setImageResource(R.drawable.logo_rskg);
        } else {
            Glide.with(context).load(urlGIF).into(holder.img_Klinik);
        }
    }

    public class KlinikViewHolder extends RecyclerView.ViewHolder {
        public TextView namaKlinik,txt_alamat, idk, urlLogo;
        public ImageView img_Klinik;
//        public ShimmerFrameLayout sfl_list_klinik;
        public LinearLayout list_layout;

        public KlinikViewHolder(View itemView) {
            super(itemView);

            namaKlinik      = itemView.findViewById(R.id.namaKlinik);
            txt_alamat      = itemView.findViewById(R.id.txt_alamat);
            img_Klinik      = itemView.findViewById(R.id.img_iconKlinik);
            urlLogo         = itemView.findViewById(R.id.urlFoto);
            idk             = itemView.findViewById(R.id.idk);

//            sfl_list_klinik = itemView.findViewById(R.id.shimmer_view);
//            list_layout     = itemView.findViewById(R.id.data_view);
//            list_layout.setVisibility(View.INVISIBLE);
//            sfl_list_klinik.startShimmerAnimation();

//            Handler hdl = new Handler();
//            hdl.postDelayed(()->{
//                list_layout.setVisibility(View.VISIBLE);
//                sfl_list_klinik.stopShimmerAnimation();
//                sfl_list_klinik.setVisibility(View.GONE);
//            },4000);

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


