package averin.sirs_rskg.com;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import averin.sirs_rskg.com.Adapter.KlinikAdapter;
import averin.sirs_rskg.com.Adapter.RequestHandler;
import averin.sirs_rskg.com.Adapter.SpinnerAdapter;
import averin.sirs_rskg.com.Model.Klinik;
import averin.sirs_rskg.com.Model.Login;
import averin.sirs_rskg.com.Model.isiSpinner;
import averin.sirs_rskg.com.Model.Token;
import averin.sirs_rskg.com.Ui.AppController;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegistbyProvinsi extends AppCompatActivity {

    ConnectivityManager conMgr;
    RecyclerView listKlinik;
    String no_ktp, val_token, np, alamat, idk, kota,telpon1, email, ket, urllogo, spnidKota, spnidProv, code_menu="5";
    String APIurl = RequestHandler.APIdev;
    ProgressDialog pDialog;
    TextView txt_login, txt_namaPasien, txt_noktp, txt_lbl, txt_data_null;
    ImageView img_data_null;
    CardView cd_data_null;
    CircleImageView fotoPasien;
    RelativeLayout  txt_infonull;
    ImageButton btn_notif;

    //Dialog Confirm
    AlertDialog.Builder dial_builder;
    AlertDialog dial_login;
    LayoutInflater inflater;
    View dialogView;

    //ELEMENT FOR SPINNER
    Spinner spnProv, spnKota;
    SpinnerAdapter spnProvadapter, spnKotaadapter;
    ArrayList<isiSpinner> listProv = new ArrayList<>();
    ArrayList<isiSpinner> listKota = new ArrayList<>();

    private ArrayList<Klinik> ArrayKlinik = new ArrayList<>();
    private KlinikAdapter Klinikadapter;
    public String urlProvinsi= APIurl+"get-data-prov.php";
    public String urlKota= APIurl+"get-data-kota.php";
    public String urlKlinik= APIurl+"get-data-klinik.php";
    public String linkJanda  = APIurl+"get-token.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registby_provinsi);

        txt_data_null = findViewById(R.id.txt_data_null);
        img_data_null = findViewById(R.id.img_data_null);
        cd_data_null = findViewById(R.id.cd_data_null);

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(

                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        // Your code goes here
                        // In this code, we are just
                        // changing the text in the textbox

                        // This line is important as it explicitly
                        // refreshes only once
                        // If "true" it implicitly refreshes forever
                        swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setColorSchemeResources(R.color.birufigma, R.color.birufigma, R.color.birufigma);
                    }
                }
        );

        //TOOLBAR
        Toolbar LabToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(LabToolbar);
//        LabToolbar.setLogoDescription(getResources().getString(R.string.app_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LabToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //CEK KONEKSI INTERNET
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE); {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        txt_infonull = findViewById(R.id.info_null);
        //GET DATA FROM CONTROLLER
        Login login = AppController.getInstance(this).getPasien();
        Token token = AppController.getInstance(this).isiToken();
        val_token = String.valueOf(token.gettoken());
        no_ktp    = String.valueOf(login.getKTP_pasien());
        cekToken();

        //Dialog ask login
        dial_builder = new AlertDialog.Builder(RegistbyProvinsi.this,R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(com.google.android.material.R.id.content);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_ask_login, viewGroup, false);
        Button btn_act_login = dialogView.findViewById(R.id.btn_login);
        Button btn_act_cancel = dialogView.findViewById(R.id.btn_cancel);
        dial_builder.setView(dialogView);
        dial_login = dial_builder.create();
        dial_login.setCancelable(false);
        btn_act_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegistbyProvinsi.this, LoginActivity.class);
                i.putExtra("email_px", "");
                i.putExtra("pass_px", "");
                i.putExtra("last_menu", code_menu);
                startActivity(i);
                dial_login.dismiss();

            }
        });
        btn_act_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dial_login.dismiss();
            }
        });

        //SPINNER PROVINSI
        spnProv = findViewById(R.id.s_provinsi);
        spnProvadapter = new SpinnerAdapter(RegistbyProvinsi.this, listProv);
        spnProv.setAdapter(spnProvadapter);
        spnProv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                spnidProv =listProv.get(position).getId();
                getSPNKota(spnidProv);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        //SPINNER KOTA
        spnKota = findViewById(R.id.s_kota);
        spnKotaadapter = new SpinnerAdapter(RegistbyProvinsi.this, listKota);
        spnKota.setAdapter(spnKotaadapter);
        //RECYLE VIEW KLINIK
        listKlinik = findViewById(R.id.listKlinik);
        listKlinik.setLayoutManager(new LinearLayoutManager(this));
        Klinikadapter = new KlinikAdapter(this, ArrayKlinik, this);
        listKlinik.setAdapter(Klinikadapter);

        spnKota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                ArrayKlinik.isEmpty();
                spnidKota =listKota.get(position).getId();
                viewKlinik(spnidKota);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void onBackPressed() {
        Intent startMain = new Intent(RegistbyProvinsi.this,MainActivity.class);
        startActivity(startMain);
    }

    public void cekToken() {
        //first getting the values
        final String isiToken    = val_token;
        final String ktp         = no_ktp;
        final String APIurl      = RequestHandler.APIdev;

        //if everything is fine
        class cekToken extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("no_ktp", ktp);

                //returing the response
                return requestHandler.requestData(APIurl+"cek-data-px.php", "POST", "application/json; charset=utf-8", "X-Api-Token",
                        isiToken, "X-Px-Key", "", params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                progressBar = (ProgressBar) findViewById(R.id.progressBar);
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
                        ambilToken();
                    } else {
                        GetSpnProv();
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
        //first getting the values
        final String KodeApi    = "MUSA";
        final String KeyApi     = "@@TTWYYW";
        final String KeyCode    = "602f07f23fc390cfd4461b268f95eddfbd4fc87d9b313b64a943801b5e4c5b12";
        final String APIurl     = RequestHandler.APIdev;

//        final String KodeApi = "privy";
//        final String KeyApi  = "@nqgsMKf";
//        final String KeyCode = "befa14f43777a6fbb55bc6dc939784202da3a4c0f43c172ac636978aec117bad";

        //if everything is fine
        class ambilToken extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("KodeApi", KodeApi);
                params.put("KeyApi", KeyApi);
                params.put("KeyCode", KeyCode);

                //returing the response
                return requestHandler.reqToken(APIurl+"get-token.php", params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                progressBar = (ProgressBar) findViewById(R.id.progressBar);
//                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                progressBar.setVisibility(View.GONE);

                try {//converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (obj.getString("code").equals("200")) {
                        //getting the user from the response
//                        JSONObject loginJson = obj.getJSONObject("response");

                        //creating a new user object
                        Token token = new Token(
//                                loginJson.getString("nama_grup")
                                obj.getString("token")
//                                obj.getString("pengguna")
                        );

                        //storing the user in shared preferences
                        AppController.getInstance(getApplicationContext()).getToken(token);
                        GetSpnProv();

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

    private void GetSpnProv() {
        //first getting the values
        final String iniToken     = val_token;
        listProv.clear();

//        pDialog = new ProgressDialog(RegistbyProvinsi.this);
//        pDialog.setCancelable(false);
//        pDialog.setMessage("Loading...");
//        showDialog();

        //if everything is fine
        class losdoll extends AsyncTask<Void, Void, String> {

            private HashMap params;
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                params = new HashMap<String, HashMap<String, String>>();
                params.put("", "");

                //returing the response
                return requestHandler.requestSpinner(urlProvinsi, "POST", "application/json; charset=utf-8", "X-Api-Token",
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
//                    JSONObject obj = new JSONObject(s);
                    JSONArray jso = new JSONArray(s);
                    for (int a = 0; a < jso.length(); a++) {
                        isiSpinner item = new isiSpinner();
                        JSONObject jr = jso.getJSONObject(a);

                        item.setId(jr.getString("idProv"));
                        item.setKet(jr.getString("nama"));

                        listProv.add(item);
                    }
                    spnProvadapter.notifyDataSetChanged();
//                    hideDialog();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        losdoll pl = new losdoll();
        pl.execute();
    }

    private void getSPNKota(String idProv) {
        //first getting the values
        final String iniToken     = val_token;
        listKota.clear();

//        pDialog = new ProgressDialog(RegistbyProvinsi.this);
//        pDialog.setCancelable(false);
//        pDialog.setMessage("Loading...");
//        showDialog();

        //if everything is fine
        class losdoll extends AsyncTask<Void, Void, String> {

            private HashMap params;
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                params = new HashMap<String, HashMap<String, String>>();
                params.put("idProv", idProv);

                //returing the response
                return requestHandler.requestSpinner(urlKota, "POST", "application/json; charset=utf-8", "X-Api-Token",
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
//                    JSONObject obj = new JSONObject(s);
                    JSONArray jso = new JSONArray(s);
                    for (int a = 0; a < jso.length(); a++) {
                        isiSpinner item = new isiSpinner();
                        JSONObject jr = jso.getJSONObject(a);

                        item.setId(jr.getString("idKota"));
                        item.setKet(jr.getString("nama"));

                        listKota.add(item);
                    }
                    spnKotaadapter.notifyDataSetChanged();
//                    hideDialog();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        losdoll pl = new losdoll();
        pl.execute();
    }

    public void viewKlinik(String idKota) {
        //first getting the values
        final String iniToken   = val_token;
        ArrayKlinik.clear();

        //if everything is fine
        class masukPakEko extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("idKota", idKota);
//                params.put("User", user);
//                params.put("Pass", pass);

                //returing the response
                return requestHandler.requestData(urlKlinik, "POST", "application/json; charset=utf-8", "X-Api-Token",
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
                    if(obj.getString("code").equals("500")){
                        listKlinik.setVisibility(View.GONE);
                        txt_data_null.setVisibility(View.VISIBLE);
                        img_data_null.setVisibility(View.VISIBLE);
                        cd_data_null.setVisibility(View.VISIBLE);

                    }else if(obj.getString("code").equals("200")){
                        listKlinik.setVisibility(View.VISIBLE);
                        txt_data_null.setVisibility(View.GONE);
                        img_data_null.setVisibility(View.GONE);
                        cd_data_null.setVisibility(View.GONE);
                        JSONArray jr = obj.getJSONArray("res");
                        for (int a = 0; a < jr.length(); a++) {
                            JSONObject jso = jr.getJSONObject(a);
                            np = jso.getString("nama_perusahaan");
                            alamat = jso.getString("alamat");
                            idk = jso.getString("id");
                            urllogo = jso.getString("logo_yankes");
                            ArrayKlinik.add(new Klinik(idk,np,alamat,urllogo));
                        }
                        Klinikadapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        masukPakEko pl = new masukPakEko();
        pl.execute();
    }

//    private void showDialog() {
//        if (!pDialog.isShowing())
//            pDialog.show();
//    }

//    private void hideDialog() {
//        if (pDialog.isShowing())
//            pDialog.dismiss();
//    }

    public void openvcs(View v){
        dial_login.show();
    }
}