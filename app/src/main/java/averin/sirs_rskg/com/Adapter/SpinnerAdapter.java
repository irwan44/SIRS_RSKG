package averin.sirs_rskg.com.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import averin.sirs_rskg.com.R;
import averin.sirs_rskg.com.Model.isiSpinner;

import java.util.List;

/**
 * Created by KUNCORO on 23/04/2017.
 */

public class SpinnerAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<isiSpinner> item;

    public SpinnerAdapter(Activity activity, List<isiSpinner> item) {
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int location) {
        return item.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.dialog_spinner, null);

        TextView pendidikan = (TextView) convertView.findViewById(R.id.txt_ket);

        isiSpinner data;
        data = item.get(position);

        pendidikan.setText(data.getKet());

        return convertView;
    }
}
