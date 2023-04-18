package averin.sirs_rskg.com;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import averin.sirs_rskg.com.Adapter.DokterPoliAdapter;
import averin.sirs_rskg.com.Adapter.ListPoliAdapter;
import averin.sirs_rskg.com.Adapter.PoliListAdapter;
import averin.sirs_rskg.com.Adapter.RequestHandler;
import averin.sirs_rskg.com.Adapter.SpinnerAdapter;
import averin.sirs_rskg.com.Model.DokterPoli;
import averin.sirs_rskg.com.Model.Login;
import averin.sirs_rskg.com.Model.Token;
import averin.sirs_rskg.com.Model.isiSpinner;
import averin.sirs_rskg.com.Ui.AppController;
import averin.sirs_rskg.com.Ui.ItemClickListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegistPoli extends AppCompatActivity {

    String val_token, kd_bagian, range_hari, kd_klinik, nm_klinik, nm_Dokter, nm_dokter, kd_dokter, idnya_dokter, kd_bag, bag, sFoto,
            jam_mulai, jam_akhir, wkt_periksa, idk, no_ktp, code_menu="1";
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    RecyclerView DokterPoliCycleview,rv_poli;
    Dialog dialog_regist_poli;
    TextView txt_login, txt_namaPasien, txt_noktp, txt_lbl, txt_null, txt_cariDokter;
    CircleImageView fotoPasien;
    ImageButton btn_notif;
    ImageButton imgbtn_home;
    ConnectivityManager conMgr;
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

    //ELEMENT FOR ListPoli
    SearchableSpinner s_poli;
    ItemClickListener itemClickListener;
    SpinnerAdapter spa;
    ProgressBar progressBar;
    CardView cv_null;
    ImageView img_null;

    private ArrayList<DokterPoli> ArrayDokter = new ArrayList<>();
    private ArrayList<isiSpinner> listPoli = new ArrayList<isiSpinner>();
    private ArrayList<isiSpinner> dataPoli = new ArrayList<isiSpinner>();
    private PoliListAdapter polilistAdapt;
    private DokterPoliAdapter DokterpoliAdapter;
    String APIurl = RequestHandler.APIdev;

    public String urlKlinikDetail = APIurl+"get-klinik-detail.php";
    public String urlGetDokter    = APIurl+"get-all-dokter-klinik.php";
    public String urlPoli         = APIurl+"get-poli.php";
    public String linkJanda       = APIurl+"get-token.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_poli);

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(

                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setColorSchemeResources(R.color.birufigma, R.color.birufigma, R.color.birufigma);
                    }
                }
        );
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

        //CEK CONNECTION
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

        txt_cariDokter  = findViewById(R.id.txt_caridokter);
        txt_null        = findViewById(R.id.txt_null_data);
        cv_null         = findViewById(R.id.cv_null_data);
        img_null        = findViewById(R.id.img_null_data);
        imgbtn_home     = findViewById(R.id.imgbtn_home);

        s_poli          = findViewById(R.id.s_poli);
        s_poli.setTitle("Pilih Poli");
        s_poli.setPositiveButton("Kembali");

        imgbtn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegistPoli.this, MainActivity.class);
                startActivity(i);
            }
        });

        //Dialog Alert
        dialog = new AlertDialog.Builder(RegistPoli.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_ask_login, null);
        dialog.setView(dialogView);

        //GET DATA FROM CONTROLLER
        Login login = AppController.getInstance(this).getPasien();
        Token token = AppController.getInstance(this).isiToken();
        val_token = (String.valueOf(token.gettoken()));
        Bundle kiriman = getIntent().getExtras();
        if (kiriman != null) {
            kd_klinik   = kiriman.get("kde_Klinik").toString();
            kd_dokter   = kiriman.get("kde_dokter").toString();
            nm_klinik   = kiriman.get("nma_Klinik").toString();
            nm_Dokter = kiriman.get("nma_dokter").toString();
            txt_cariDokter.setText(nm_Dokter);
//            GetSpnPoli(kd_klinik);
            viewDokter("",kd_dokter);
            GetPoliList();
        }
        if(txt_cariDokter.equals("")){
            txt_cariDokter.setText("Cari dokter");
            cekToken();
        }

        //RECYLEVIEW DOKTER POLI
        DokterpoliAdapter = new DokterPoliAdapter(ArrayDokter);
        DokterPoliCycleview = findViewById(R.id.DokterPoli_viewCycle);
        DokterPoliCycleview.setLayoutManager(new LinearLayoutManager(this));
        DokterPoliCycleview.setAdapter(DokterpoliAdapter);
        //RECYLEVIEW LIST POLI
        // Initialize listener
//        rv_poli = findViewById(R.id.rv_poli);
//        for (int i = 0; i < 20; i++) {
//            // add values in array list
//            isiSpinner item = new isiSpinner();
//            item.setId("kode_bagian"+i);
//            item.setKet("nama_bagian");
//            listPoli.add(item);
//        }

        itemClickListener = new ItemClickListener() {
            @Override public void onClick(String s)
            {
                // Notify adapter
                rv_poli.post(new Runnable() {
                    @Override public void run()
                    {
                        polilistAdapt.notifyDataSetChanged();
                    }
                });
                dataPoli.isEmpty();
                viewDokter(s,"");

            }
        };

//        rv_poli.setLayoutManager(new LinearLayoutManager(RegistPoli.this, LinearLayoutManager.HORIZONTAL, false));
//        polilistAdapt = new PoliListAdapter(listPoli, itemClickListener);
//        rv_poli.setAdapter(polilistAdapt);

//        spa = new SpinnerAdapter(RegistPoli.this, dataPoli);
//        s_poli.setAdapter(spa);
        s_poli.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                kd_bagian = dataPoli.get(position).getId();
                bag = dataPoli.get(position).getKet();
                itemClickListener.equals("");
                viewDokter(kd_bagian,"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

    public void onBackPressed() {
        Intent startMain = new Intent(RegistPoli.this,MainActivity.class);
        startActivity(startMain);
    }

    public void cekToken() {
        //first getting the values
        final String isiToken    = val_token;
        final String ktp         = no_ktp;
        final String APIurl      = RequestHandler.APIdev;

        //if everything is fine
        class cekToken extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("no_ktp", ktp);
                return requestHandler.requestData(APIurl+"cek-data-px.php", "POST", "application/json; charset=utf-8", "X-Api-Token",
                        isiToken, "X-Px-Key", "", params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {//converting response to json object
                    JSONObject obj = new JSONObject(s);
                    if (obj.getString("code").equals("500")) {
                        ambilToken();
                    } else {
//                        viewDokter("","");
                        GetPoliList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        cekToken pl = new cekToken();
        pl.execute();
    }

    public void ambilToken() {
        final String KodeApi    = "MUSA";
        final String KeyApi     = "@@TTWYYW";
        final String KeyCode    = "602f07f23fc390cfd4461b268f95eddfbd4fc87d9b313b64a943801b5e4c5b12";
        final String APIurl     = RequestHandler.APIdev;
        //if everything is fine
        class ambilToken extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("KodeApi", KodeApi);
                params.put("KeyApi", KeyApi);
                params.put("KeyCode", KeyCode);
                return requestHandler.reqToken(APIurl+"get-token.php", params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {

                super.onPostExecute(s);
                try {//converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (obj.getString("code").equals("200")) {
                        //creating a new user object
                        Token token = new Token(
                                obj.getString("token")
                        );

                        AppController.getInstance(getApplicationContext()).getToken(token);
//                        viewDokter("","");
                        GetPoliList();

                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        ambilToken pl = new ambilToken();
        pl.execute();
    }

    private void GetPoliList() {
        //first getting the values
        final String iniToken     = val_token;
        final String kodeRS       = kd_klinik;
        listPoli.clear();

        //if everything is fine
        class losdoll extends AsyncTask<Void, Void, String> {

            private HashMap params;
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                params = new HashMap<String, HashMap<String, String>>();
                params.put("kode_klinik", kodeRS);

                //returing the response
                return requestHandler.requestData(urlPoli, "POST", "application/json; charset=utf-8", "X-Api-Token",
                        iniToken, "X-Px-Key", "", params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);

                try {//converting response to json object
                    List<String> dt_poli = new ArrayList<>();
                    String kode_bag;
                    JSONObject obj = new JSONObject(s);
                    JSONArray jso = obj.getJSONArray("list");
                    for (int a = 0; a < jso.length(); a++) {

                        isiSpinner item = new isiSpinner();
                        JSONObject jr = jso.getJSONObject(a);
                        item.setId(jr.getString("kode_bagian"));
                        item.setKet(jr.getString("nama_bagian"));
                        dataPoli.add(item);
                        dt_poli.add(jr.getString("nama_bagian"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplication(), R.layout.dialog_spinner,dt_poli);
                    s_poli.setAdapter(adapter);
//                    spa.notifyDataSetChanged();
//                    hideDialog();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        losdoll pl = new losdoll();
        pl.execute();
    }

    private void viewDokter(final String filter_kdBagian, final String kde_Dokter) {
        //first getting the values
        final String kodeRS  = kd_klinik;
        final String iniToken   = val_token;
        final String order      = "asc";
        final String offset     = "0";
        final String limit      = "10";
        ArrayDokter.clear();

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
                val.put("filter", filter_kdBagian);
                val.put("kdeDokter", kde_Dokter);
                val.put("order", order);
                val.put(offset, offset);
                val.put("limit", limit);
                params.put("kode_klinik",kodeRS);
                params.put("filter", filter_kdBagian);
                //returing the response
                return requestHandler.requestData(urlGetDokter, "POST", "application/json; charset=utf-8", "X-Api-Token",
                        iniToken, "X-Px-Key", "", params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);

                try {//converting response to json object
                    JSONObject obj = new JSONObject(s);
                    //if no error in response
                    if (obj.getString("count").equals("0")) {
                        DokterPoliCycleview.setVisibility(View.GONE);
                        txt_null.setVisibility(View.VISIBLE);
                        cv_null.setVisibility(View.VISIBLE);
                        img_null.setVisibility(View.VISIBLE);
                    } else {
                        DokterPoliCycleview.setVisibility(View.VISIBLE);
                        txt_null.setVisibility(View.GONE);
                        cv_null.setVisibility(View.GONE);
                        img_null.setVisibility(View.GONE);
                        JSONArray jr = obj.getJSONArray("items");
                        for (int a = 0; a < jr.length(); a++) {
                            txt_null.setVisibility(View.GONE);
                            JSONObject jso = jr.getJSONObject(a);
                            nm_dokter    = jso.getString("nama_pegawai");
                            kd_dokter    = jso.getString("kode_dokter");
                            idnya_dokter = jso.getString("id");
                            kd_bag       = jso.getString("kode_bagian");
                            bag          = jso.getString("nama_bagian");
                            jam_mulai    = jso.getString("jam_mulai");
                            jam_akhir    = jso.getString("jam_akhir");
                            wkt_periksa  = jso.getString("waktu_periksa");
//                            range_hari   = jso.getString("range_hari");
                            sFoto        = jso.getString("foto");

                            ArrayDokter.add(new DokterPoli("C00003", nm_klinik, nm_dokter,
                                    kd_dokter, idnya_dokter, kd_bag, bag, jam_mulai, jam_akhir,
                                    wkt_periksa, sFoto));
                        }
                        DokterpoliAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        masukPakEko pl = new masukPakEko();
        pl.execute();
    }

    public void openvcs(View v) {
        dialog.setCancelable(true);
        dialog.show();
    }

    public void actLogin(View v) {
        Intent i = new Intent(RegistPoli.this, LoginActivity.class);
        i.putExtra("last_menu", "3");
        startActivity(i);
    }

    public void cariDokter(View view) {
        Intent i = new Intent(RegistPoli.this, listcariDokter.class);
        i.putExtra("kodeKlinik", kd_klinik);
        i.putExtra("namaKlinik", nm_klinik);
        startActivity(i);
    }

//    private void showDialog() {
//        if (!pDialog.isShowing())
//            pDialog.show();
//    }

//    private void hideDialog() {
//        if (pDialog.isShowing())
//            pDialog.dismiss();
//    }
}