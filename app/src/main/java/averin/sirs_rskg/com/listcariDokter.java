package averin.sirs_rskg.com;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import averin.sirs_rskg.com.Adapter.CariDokterAdapter;
import averin.sirs_rskg.com.Adapter.RequestHandler;
import averin.sirs_rskg.com.Model.DokterPoli;
import averin.sirs_rskg.com.Model.Token;
import averin.sirs_rskg.com.Ui.AppController;

public class listcariDokter extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    String val_token, kodeKlinik, namaKlinik, kd_klinik, nm_klinik, nm_Dokter, nm_dokter, kd_dokter, idnya_dokter,
            kd_bag, bag, sFoto, jam_mulai, jam_akhir, wkt_periksa, idk, range_hari;

    ProgressDialog pDialog;
    List<DokterPoli> caridokter = new ArrayList<>();
    CariDokterAdapter adapter;
    SwipeRefreshLayout swipe;
    ConnectivityManager conMgr;
    ListView list_view;
    androidx.appcompat.widget.SearchView search;
    String APIurl = RequestHandler.APIdev;
    public String urlCariDokter = APIurl+"get-dokter-klinik.php";
    public String urlAllDokter = APIurl+"get-all-dokter-klinik.php";
    public String linkJanda  = APIurl+"get-token.php";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_dokter);
        swipe       = findViewById(R.id.swipe_refresh);
        list_view   = findViewById(R.id.lv_dokterPoli);
        search      = findViewById(R.id.searchDokter);
        adapter     = new CariDokterAdapter(listcariDokter.this, caridokter);
        list_view.setAdapter(adapter);
        swipe.setOnRefreshListener(this);
        search.setQuery("", true);
        search.setFocusable(true);
        search.setIconified(false);
        search.requestFocusFromTouch();

        //TOOLBAR
        Toolbar LabToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(LabToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LabToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        Bundle data = getIntent().getExtras();
        if(data!=null){
            kodeKlinik = data.get("kodeKlinik").toString();
            namaKlinik = data.get("namaKlinik").toString();
        }

        Token tkn = AppController.getInstance(this).isiToken();
        val_token = String.valueOf(tkn.gettoken());

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(listcariDokter.this, DaftarPoli.class);
//                idDokter = caridokter.get(position).getIdDokter();
//                kdDokter = caridokter.get(position).getkodeDokter();
//                namaDokter = caridokter.get(position).getnamaDokter();
//                i.putExtra("kde_Klinik",kodeKlinik);
//                i.putExtra("kde_dokter", kdDokter);
//                i.putExtra("nma_Klinik",namaKlinik);
//                i.putExtra("nma_dokter", namaDokter);
//                startActivity(i);

                String urlFoto = caridokter.get(position).getFoto_dokter();
                String kode_dokter = caridokter.get(position).getKode_dokter();
                String idnya_dokter = caridokter.get(position).getIdnya_dokter();
                String nama_pegawai = caridokter.get(position).getNama_dokter();
                String kode_bagian = caridokter.get(position).getKode_bag();
                String nama_bagian = caridokter.get(position).getBagian();
                String kde_klinik = caridokter.get(position).getKd_klinik();
                String nma_klinik = caridokter.get(position).getNm_klinik();
                String durasi = caridokter.get(position).getWaktu_periksa();
                i.putExtra("urlFoto", urlFoto);
                i.putExtra("kd_dokter", kode_dokter);
                i.putExtra("idnya_dokter", idnya_dokter);
                i.putExtra("nm_dokter", nama_pegawai);
                i.putExtra("kd_bag", kode_bagian);
                i.putExtra("nm_bag", nama_bagian);
                i.putExtra("kd_klinik", kodeKlinik);
                i.putExtra("nm_klinik", namaKlinik);
                i.putExtra("durasi", durasi);
                startActivity(i);

            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String keyword) {
                cariData(keyword);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String keyword) {
                cariData(keyword);
                return false;
            }
        });

        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(true);
                ListData();
            }
        });
    }

    private void cariData(final String keyword) {
        //first getting the values
        final String iniToken  = val_token;
        final String kdeKlinik = kodeKlinik;

        //if everything is fine
        class masukPakEko extends AsyncTask<Void, Void, String> {

            private HashMap params;
            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                params = new HashMap<String, HashMap<String, String>>();
                HashMap<String, String> val = new HashMap<String, String>();
//                val.put("filter", keyword);
                params.put("kode_klinik", kdeKlinik);
                params.put("filter", keyword);
                //returing the response
                return requestHandler.requestData(urlCariDokter, "POST", "application/json; charset=utf-8", "X-Api-Token",
                        iniToken, "X-Px-Key", "", params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                progressBar = findViewById(R.id.progressBar);
//                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                progressBar.setVisibility(View.GONE);

                try {//converting response to json object
                    JSONObject obj = new JSONObject(s);
                    //if no error in response
                    if (obj.getString("code").equals("500")) {
                        Toast.makeText(getApplicationContext(), obj.getString("msg"),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        caridokter.clear();
                        adapter.notifyDataSetChanged();
                        JSONArray jr = obj.getJSONArray("items");
                        for (int a = 0; a < jr.length(); a++) {
                            JSONObject jso = jr.getJSONObject(a);

//                            idDokter    = jso.getString("id_dokter");
//                            kdDokter    = jso.getString("kode_dokter");
//                            namaDokter  = jso.getString("nama_dokter");
//                            caridokter.add(new CariDokter(idDokter,kdDokter,namaDokter));

                            nm_dokter    = jso.getString("nama_pegawai");
                            kd_dokter    = jso.getString("kode_dokter");
                            idnya_dokter = jso.getString("id");
                            kd_bag       = jso.getString("kode_bagian");
                            bag          = jso.getString("nama_bagian");
                            jam_mulai    = jso.getString("jam_mulai");
                            jam_akhir    = jso.getString("jam_akhir");
                            wkt_periksa  = jso.getString("waktu_periksa");
                            range_hari  = jso.getString("range_hari");
                            sFoto        = jso.getString("foto");

                            caridokter.add(new DokterPoli(kd_klinik, nm_klinik, nm_dokter,
                                    kd_dokter, idnya_dokter, kd_bag, bag, jam_mulai, jam_akhir,
                                    wkt_periksa,range_hari, sFoto));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        masukPakEko pl = new masukPakEko();
        pl.execute();
    }

    private void ListData() {
        //first getting the values
        final String iniToken  = val_token;
        final String kdeKlinik = kodeKlinik;

        //if everything is fine
        class masukPakEko extends AsyncTask<Void, Void, String> {

            private HashMap params;
            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                params = new HashMap<String, HashMap<String, String>>();
                HashMap<String, String> val = new HashMap<String, String>();
                params.put("kode_klinik", kdeKlinik);
                //returing the response
                return requestHandler.requestData(urlAllDokter, "POST", "application/json; charset=utf-8", "X-Api-Token",
                        iniToken, "X-Px-Key", "", params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                progressBar = findViewById(R.id.progressBar);
//                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                progressBar.setVisibility(View.GONE);

                try {//converting response to json object
                    JSONObject obj = new JSONObject(s);
                    //if no error in response
                    if (obj.getString("code").equals("500")) {
                        String pesan = obj.getString("msg");
                        Toast.makeText(listcariDokter.this,
                                pesan, Toast.LENGTH_SHORT).show();
                    } else {
                        caridokter.clear();
                        adapter.notifyDataSetChanged();
                        swipe.setRefreshing(true);
                        JSONArray jr = obj.getJSONArray("items");
                        for (int a = 0; a < jr.length(); a++) {
                            JSONObject jso = jr.getJSONObject(a);
//                            idDokter    = jso.getString("id_dokter");
//                            kdDokter    = jso.getString("kode_dokter");
//                            namaDokter  = jso.getString("nama_dokter");
//                            caridokter.add(new CariDokter(idDokter,kdDokter,namaDokter));

                            nm_dokter    = jso.getString("nama_pegawai");
                            kd_dokter    = jso.getString("kode_dokter");
                            idnya_dokter = jso.getString("id");
                            kd_bag       = jso.getString("kode_bagian");
                            bag          = jso.getString("nama_bagian");
                            jam_mulai    = jso.getString("jam_mulai");
                            jam_akhir    = jso.getString("jam_akhir");
                            wkt_periksa  = jso.getString("waktu_periksa");
                            range_hari  = jso.getString("range_hari");
                            sFoto        = jso.getString("foto");

                            caridokter.add(new DokterPoli(kd_klinik, nm_klinik, nm_dokter,
                                    kd_dokter, idnya_dokter, kd_bag, bag, jam_mulai, jam_akhir,
                                    wkt_periksa,range_hari ,sFoto));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    swipe.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        masukPakEko pl = new masukPakEko();
        pl.execute();
    }

    @Override
    public void onRefresh() {
        ListData();
    }
}
