package averin.sirs_rskg.com.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import averin.sirs_rskg.com.Model.FotoRad;
import averin.sirs_rskg.com.R;

public class FotoRadAdapter extends RecyclerView.Adapter<FotoRadAdapter.RadFotoViewHolder> {

    private ArrayList<FotoRad> list;
    private byte[] bytIMGrad;
    private String urlIMG;
    public Context context;

    public FotoRadAdapter(Activity activity, ArrayList<FotoRad> list, Context context) {
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
    public RadFotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View view = li.inflate(R.layout.list_foto_rad, parent, false);
        return new RadFotoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RadFotoViewHolder holder, int position) {
        holder.txt_no_urut.setText(list.get(position).getNo_urut());
        holder.txt_urlfoto.setText(list.get(position).getUrl_imgrad());
        holder.txt_namatindakan.setText(list.get(position).getNamatindakan());
//        holder.txt_namatindakan.setText("slslslsl");
        urlIMG = holder.txt_urlfoto.getText().toString();
        Glide.with(context).load(urlIMG).into(holder.foto_rad);

    }

    public class RadFotoViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_no_urut, txt_urlfoto, txt_namatindakan;
        public ImageView foto_rad;

        public RadFotoViewHolder(final View itemView) {
            super(itemView);

            txt_urlfoto       = itemView.findViewById(R.id.url_foto);
            txt_no_urut       = itemView.findViewById(R.id.txt_nourut);
            txt_namatindakan  = itemView.findViewById(R.id.txt_namatindakan);
            foto_rad        = itemView.findViewById(R.id.img_foto_rad);

        }
    }
}
